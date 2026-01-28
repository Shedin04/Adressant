package com.shedin.adressant.sender;

import com.shedin.adressant.sender.helpers.EmailChecker;
import com.shedin.adressant.sender.helpers.IPAddressChecker;
import com.shedin.adressant.sender.helpers.MXRecordsChecker;
import com.shedin.adressant.sender.helpers.RegexHelper;
import com.shedin.adressant.sender.helpers.WhoisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SenderService {

    private final SenderRepository senderRepository;
    private final EmailChecker emailChecker;
    private final MXRecordsChecker mxRecordsChecker;
    private final WhoisHelper whoisHelper;
    private final IPAddressChecker ipAddressChecker;
    private final RegexHelper regexHelper;

    public SenderResponse getSenderInfo(String email) {
        if (StringUtils.isBlank(email)) {
            log.warn("Blank email was send");
            throw new IllegalArgumentException("Incorrect input");
        } else {
            String domain = regexHelper.getDomainFromEmail(email);
            Optional<Sender> senderOptional = senderRepository.findByDomainName(domain);
            if (senderOptional.isPresent()) {
                return returnExistingSender(senderOptional.get());
            } else {
                registerNewDomain(domain);
                return getSenderInfo(domain);
            }
        }
    }

    private void registerNewDomain(String domain) {
        Sender sender = createNewSender(domain);
        senderRepository.save(sender);
    }

    private SenderResponse returnExistingSender(Sender sender) {
        String senderDomain = sender.getDomainName();
        List<String> mxRecords = mxRecordsChecker.getMxRecords(senderDomain);
        if (!(sender.isTemporary() && sender.isFree())) {
            if (emailChecker.isTemporary(senderDomain)) { //To update status if it was change
                sender.setTemporary(true);
                log.info("Temporary status for '{}' was changed", senderDomain);
            }
            if (emailChecker.isFree(senderDomain)) {
                sender.setFree(true);
                log.info("Free status for '{}' was changed", senderDomain);
            }
        }
        int newNumberOfScans = sender.getNumberOfScans() + 1;
        sender.setNumberOfScans(newNumberOfScans);

        LocalDateTime lastScanTime = sender.getLastScanTime();
        sender.setLastScanTime(LocalDateTime.now(ZoneOffset.UTC));

        senderRepository.save(sender);

        String ipAddress = ipAddressChecker.getDomainIP(senderDomain);
        List<String> whoisInfo = whoisHelper.getDomainWhoisLookup(senderDomain);
        return SenderResponse.builder()
                .domainId(sender.getDomainId())
                .domain(sender.getDomainName())
                .numberOfScans(sender.getNumberOfScans())
                .lastScanTime(lastScanTime)
                .isTemporary(sender.isTemporary())
                .isFree(sender.isFree())
                .ipAddress(ipAddress)
                .mxRecords(mxRecords)
                .whoisInfo(whoisInfo)
                .rate(sender.getDomainRating()).build();
    }

    private Sender createNewSender(String email) {
        boolean isTemporary = emailChecker.isTemporary(email);
        boolean isFree = emailChecker.isFree(email);
        LocalDateTime initialScanTime = LocalDateTime.now(ZoneOffset.UTC);
        return new Sender(email, isTemporary, isFree, initialScanTime);
    }

    public EmailRating updateEmailRating(EmailRating emailRating) {
        Optional<Sender> senderOptional = senderRepository.findByDomainId(emailRating.getDomainId());
        if (senderOptional.isPresent()) {
            Sender sender = senderOptional.get();
            double newRating = getNewRating(sender, emailRating.getDomainRating());
            sender.setDomainRating(newRating);
            sender.setNumberOfRating(sender.getNumberOfRating() + 1);
            senderRepository.save(sender);
            emailRating.setDomainRating(newRating);
            return emailRating;
        } else {
            log.warn("Cannot update rating for '{}' as it doesn't exist", emailRating.getDomainId());
            return null;
        }
    }

    private double getNewRating(Sender sender, double newValue) {
        BigDecimal numberOfRating = BigDecimal.valueOf(sender.getNumberOfRating());
        BigDecimal total = BigDecimal.valueOf(sender.getDomainRating()).multiply(numberOfRating);
        total = total.add(BigDecimal.valueOf(newValue));
        return total.divide(numberOfRating.add(BigDecimal.ONE), 1, RoundingMode.HALF_UP).doubleValue();
    }
}