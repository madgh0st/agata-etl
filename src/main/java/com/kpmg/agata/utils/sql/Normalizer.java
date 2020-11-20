package com.kpmg.agata.utils.sql;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Normalizer implements Serializable {
    private static final Map<String, String> MAP_PUNCT = new HashMap<>();
    private static final Map<String, String> MAP_CUSTOM = new HashMap<>();
    private static final List<String> REGEX_TO_REMOVE = new ArrayList<>();
    private static final char[] TRIM = new char[]{' ', '-'};

    static {
        MAP_PUNCT.put("'", "");
        MAP_PUNCT.put("\"", "");
        MAP_PUNCT.put("«", "");
        MAP_PUNCT.put("»", "");
        MAP_PUNCT.put(",", "");
        MAP_PUNCT.put("”", "");
        MAP_PUNCT.put("“", "");
        MAP_PUNCT.put(";", "");
        MAP_PUNCT.put("–", "-");

        MAP_CUSTOM.put("ГАЗПРОМНЕФТЬ", "ГАЗПРОМ НЕФТЬ");
        MAP_CUSTOM.put("Г.ТВЕРЬ", "");
        MAP_CUSTOM.put("Г.МОСКВА", "");
        MAP_CUSTOM.put("ТОРГОВЫЙ ДОМ", "ТД");

        List<String> wordsToRemove = new ArrayList<>();
        wordsToRemove.add("ООО");    // russian letters
        wordsToRemove.add("OOO");    // english letters
        wordsToRemove.add("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ");
        wordsToRemove.add("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬ");
        wordsToRemove.add("ЗАО");
        wordsToRemove.add("ЗАКРЫТОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО");
        wordsToRemove.add("ОАО");
        wordsToRemove.add("ОТКРЫТОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО");
        wordsToRemove.add("ПАО");
        wordsToRemove.add("(ПУБЛИЧНОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО)");
        wordsToRemove.add("ПУБЛИЧНОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО");
        wordsToRemove.add("(ПАО)");
        wordsToRemove.add("АО");
        wordsToRemove.add("АКЦИОНЕРНОЕ ОБЩЕСТВО");
        wordsToRemove.add("АКЦИОНЕРНОГО ОБЩЕСТВА");
        wordsToRemove.add("ИП");
        wordsToRemove.add("И.П.");
        wordsToRemove.add("ИНДИВИДУАЛЬНЫЙ ПРЕДПРИНИМАТЕЛЬ");
        wordsToRemove.add("МУП");
        wordsToRemove.add("МУНИЦИПАЛЬНОЕ УНИТАРНОЕ ПРЕДПРИЯТИЕ");
        wordsToRemove.add("УМП");
        wordsToRemove.add("УНИТАРНОЕ МУНИЦИПАЛЬНОЕ ПРЕДПРИЯТИЕ");
        wordsToRemove.add("МП");
        wordsToRemove.add("ГП");
        wordsToRemove.add("ФГБНУ");
        wordsToRemove.add("ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ НАУЧНОЕ УЧРЕЖДЕНИЕ");
        wordsToRemove.add("ФБУ");
        wordsToRemove.add("ФЕДЕРАЛЬНОЕ БЮДЖЕТНОЕ УЧРЕЖДЕНИЕ");
        wordsToRemove.add("ГУП");
        wordsToRemove.add("ГОСУДАРСТВЕННОЕ УНИТАРНОЕ ПРЕДПРИЯТИЕ");
        wordsToRemove.add("СХПК");
        wordsToRemove.add("СПК");
        wordsToRemove.add("СЕЛЬСКОХОЗЯЙСТВЕННЫЙ ПРОИЗВОДСТВЕННЫЙ КООПЕРАТИВ");
        wordsToRemove.add("КФХ");
        wordsToRemove.add("КРЕСТЬЯНСКОЕ ФЕРМЕРСКОЕ ХОЗЯЙСТВО");
        wordsToRemove.add("ФЕРМЕРСКОЕ ХОЗЯЙСТВО");
        wordsToRemove.add("КЗ");
        wordsToRemove.add("КОЛХОЗ");
        wordsToRemove.add("ФКУ");
        wordsToRemove.add("ФЕДЕРАЛЬНОЕ КАЗЕННОЕ УЧРЕЖДЕНИЕ");
        wordsToRemove.add("УФСИН");
        wordsToRemove.add("УПРАВЛЕНИЕ ФЕДЕРАЛЬНОЙ СЛУЖБЫ ИСПОЛНЕНИЯ НАКАЗАНИЙ");
        wordsToRemove.add("ПРОИЗВОДСТВЕННОЕ ГЕОФИЗИЧЕСКОЕ ОБЪЕДИНЕНИЕ");
        wordsToRemove.add("ПРОИЗВОДСТВЕННО ГЕОФИЗИЧЕСКОЕ ОБЪЕДИНЕНИЕ");
        wordsToRemove.add("ПРОИЗВОДСТВЕННО-ГЕОФИЗИЧЕСКОЕ ОБЪЕДИНЕНИЕ");
        wordsToRemove.add("ФГУП");
        wordsToRemove.add("ФГБУ");

        wordsToRemove.add("CORPORATION JSC");
        wordsToRemove.add("JOINT STOCK COMPANY");
        wordsToRemove.add("LIMITED PARTNERSHIP");
        wordsToRemove.add("LIMITED LIABILITY PARTNERSHIP");
        wordsToRemove.add("LIMITED LIABILITY LIMITED PARTNERSHIP");
        wordsToRemove.add("LIMITED LIABILITY COMPANY");
        wordsToRemove.add("PROFESSIONAL LIMITED LIABILITY COMPANY");
        wordsToRemove.add("INCORPORATED");
        wordsToRemove.add("LIMITED");
        wordsToRemove.add("PROPRIETARY LIMITED COMPANY");
        wordsToRemove.add("UNLIMITED PROPRIETARY");
        wordsToRemove.add("CORPORATION");
        wordsToRemove.add("LP");
        wordsToRemove.add("LLP");
        wordsToRemove.add("LLLP");
        wordsToRemove.add("LLC");
        wordsToRemove.add("LC");
        wordsToRemove.add("PLLC");
        wordsToRemove.add("INC");
        wordsToRemove.add("PTY");

        wordsToRemove.stream()
                .map(word -> "\\b" + word + "\\b")
                .forEach(REGEX_TO_REMOVE::add);

        REGEX_TO_REMOVE.add("\\bLTD\\b\\.?");       // LTD or LTD.
        REGEX_TO_REMOVE.add("\\bЛТД\\b\\.?");
        REGEX_TO_REMOVE.add("\\bPTE\\b\\.?");
        REGEX_TO_REMOVE.add("\\bПТЕ\\b\\.?");
        REGEX_TO_REMOVE.add("\\bПТИ\\b\\.?");
        REGEX_TO_REMOVE.add("\\bК\\(Ф\\)Х\\b");     // К(Ф)Х
        REGEX_TO_REMOVE.add("[(][^()]*[)]");        // anything in round brackets "()" including brackets
    }

    public LocalDate convertToLocalDate(String rawDate) {
        try {
            return LocalDate.parse(rawDate);
        } catch (Exception ex) {
            return null;
        }
    }

    public String normalize(String rawString) {

        if (rawString == null) return null;

        String cleanString = rawString.toUpperCase();

        for (Map.Entry<String, String> entry : MAP_PUNCT.entrySet()) {
            String target = entry.getKey();
            String replacement = entry.getValue();
            cleanString = cleanString.replaceAll(target, replacement);
        }

        for (Map.Entry<String, String> entry : MAP_CUSTOM.entrySet()) {
            String target = entry.getKey();
            String replacement = entry.getValue();
            cleanString = cleanString.replaceAll("\\b" + target + "\\b", replacement);
        }

        for (String regexToRemove : REGEX_TO_REMOVE) {
            cleanString = cleanString.replaceAll(regexToRemove, " ");
        }

        cleanString = cleanString
                .replaceAll("\\s+", " ")
                .toUpperCase();

        cleanString = StringUtils.strip(cleanString, String.valueOf(TRIM));
        return cleanString;
    }

    public String getValueDateToString(String rawDate) {
        String formatDate = "";

        try {
            if (StringUtils.isNotBlank(rawDate)) {
                Date javaDate = DateUtil.getJavaDate(Double.parseDouble(rawDate.replace(',', '.')));
                formatDate = new SimpleDateFormat("yyyy-MM-dd").format(javaDate);
            }
        } catch (Exception ex) {
            return "";
        }
        return formatDate;
    }
}
