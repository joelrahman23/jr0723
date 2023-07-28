package com.tools.checkout.service;

import com.google.common.collect.ImmutableMap;
import com.tools.checkout.model.RentalAgreement;
import com.tools.checkout.model.Tool;
import com.tools.checkout.model.ToolRental;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalAgreementService {
    private static Calendar cacheCalendar = Calendar.getInstance();

    public RentalAgreement calculateCharges(String toolCode, LocalDate checkoutDate, int rentalDays, int discountPercent) {

        RentalAgreement rentalAgreement = new RentalAgreement();
        ImmutableMap<String, Tool> toolMap = rentalAgreement.getToolMap();
        ImmutableMap<String, ToolRental> rentalMap = rentalAgreement.getRentalMap();

        if (!rentalAgreement.getToolMap().containsKey(toolCode)) {
            throw new IllegalArgumentException("Error: The tool code is invalid");
        }

        Tool tool = toolMap.get(toolCode);
        ToolRental toolRental = rentalMap.get(tool.getToolType());

        // finding effective days
        int chargeDays = getChargeDays(checkoutDate, rentalDays, toolRental);

        // calculating charging
        rentalAgreement.setPreDiscountCharge(chargeDays * toolRental.getDailyCharge());
        rentalAgreement.setDiscountPercent(discountPercent);
        rentalAgreement.setDiscountAmount(rentalAgreement.getPreDiscountCharge() * discountPercent / 100.00);
        rentalAgreement.setFinalCharge(rentalAgreement.getPreDiscountCharge() - rentalAgreement.getDiscountAmount());

        rentalAgreement.setToolCode(toolCode);
        rentalAgreement.setToolType(tool.getToolType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setChargeDays(chargeDays);
        rentalAgreement.setRentalDays(rentalDays);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setDueDate(checkoutDate.plusDays(rentalDays+1));
        rentalAgreement.setDailyRentalCharge(toolRental.getDailyCharge());

        return rentalAgreement;
    }

    public void printRentalAgreement(RentalAgreement rentalAgreement) {
        StringBuilder builder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);

        builder.append("Tool code: ").append(rentalAgreement.getToolCode()).append("\n");
        builder.append("Tool type: ").append(rentalAgreement.getToolType()).append("\n");
        builder.append("Tool brand: ").append(rentalAgreement.getToolBrand()).append("\n");
        builder.append("Rental days: ").append(rentalAgreement.getRentalDays()).append("\n");
        builder.append("Checkout date: ").append(rentalAgreement.getCheckoutDate()).append("\n");
        builder.append("Due date: ").append(rentalAgreement.getDueDate()).append("\n");
        builder.append("Daily rental charge: ").append(currencyFormat.format(rentalAgreement.getDailyRentalCharge())).append("\n");
        builder.append("Charge days: ").append(rentalAgreement.getChargeDays()).append("\n");
        builder.append("Pre-discount charge: ").append(currencyFormat.format(rentalAgreement.getPreDiscountCharge())).append("\n");
        builder.append("Discount percent: ").append(percentFormat.format(rentalAgreement.getDiscountPercent() / 100.0)).append("\n");
        builder.append("Discount amount: ").append(currencyFormat.format(rentalAgreement.getDiscountAmount())).append("\n");
        builder.append("Final charge: ").append(currencyFormat.format(rentalAgreement.getFinalCharge())).append("\n");

        log.info(builder.toString());
    }

    private int getChargeDays(LocalDate checkoutDate, int rentalDays, ToolRental toolRental) {
        int chargeDays = 0;

        for (int i = 0; i < rentalDays; i++) {
            LocalDate currentDate = checkoutDate.plusDays(i);

            log.info("Day: {}, of rental: {}", currentDate.getDayOfWeek(), rentalDays);

            if (toolRental.isWeekdayCharge() && !isWeekEnd(currentDate)) {
                if(!isLaborDay(currentDate)){
                    chargeDays++;
                }
            } else if (toolRental.isHolidayCharge()) {
                if (currentDate.getDayOfWeek() == DayOfWeek.MONDAY && (isIndependenceDaySunday(currentDate.minusDays(1)) || isLaborDay(currentDate))) {
                    chargeDays++;
                } else if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY && isIndependenceDaySaturday(currentDate.plusDays(1))) {
                    chargeDays++;
                }
            } else if (toolRental.isWeekendCharge()) {
                chargeDays++;
            }
        }
        return chargeDays;
    }

    private boolean isIndependenceDaySaturday(LocalDate date) {
        return date.getMonthValue() == 7 && date.getDayOfMonth() == 4 && date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    private boolean isIndependenceDaySunday(LocalDate date) {
        return date.getMonthValue() == 7 && date.getDayOfMonth() == 4 && date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean isLaborDay(LocalDate date) {
        return date.getDayOfMonth() == getFirstMonday(date.getYear(), Calendar.SEPTEMBER);
    }

    public int getFirstMonday(int year, int month) {

        cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        cacheCalendar.set(Calendar.MONTH, month);
        cacheCalendar.set(Calendar.YEAR, year);
        return cacheCalendar.get(Calendar.DATE);
    }

    public static boolean isWeekEnd(LocalDate localDate) {
        String dayOfWeek = localDate.getDayOfWeek().toString();
        if ("SATURDAY".equalsIgnoreCase(dayOfWeek) ||
                "SUNDAY".equalsIgnoreCase(dayOfWeek)) {
            return true;
        }
        return false;
    }
}
