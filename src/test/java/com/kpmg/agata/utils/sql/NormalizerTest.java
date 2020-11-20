package com.kpmg.agata.utils.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NormalizerTest {

    private Normalizer n = new Normalizer();

    @Test
    public void normalizeTest() {
        assertEquals("OAO ГАЗПРОМ НЕФТЬ DD-WW", n.normalize(" OAO ; газпром нефть «dd–ww»   "));
        assertEquals("КОНТРАГЕНТ РОМАШКА", n.normalize(" контрагент АО \"Ромашка\" г.москва"));
        assertEquals("КОНТРАГЕНТ РОМАШКА", n.normalize(" контрагент ао \"Ромашка\" г.москва"));
        assertEquals("УРАЛ-НЭТ", n.normalize("урал-нэт зао"));
        assertEquals("УРАЛ-НЭТ", n.normalize("зао \"урал-нэт\""));
        assertEquals("ЛАЗАРЕВ О.В.", n.normalize("ЛАЗАРЕВ О.В. (ип)"));
        assertEquals("ПРОГРЕСС", n.normalize("Закрытое акционерное общество Прогресс"));
        assertEquals("COMPANY-1", n.normalize("Company-1 Ltd"));
        assertEquals("COMPANY-1", n.normalize("Company-1 Ltd."));
        assertEquals("COMPANY-2", n.normalize("Company-2 (ex. Company-1)"));
        assertEquals("COMPANY-2 ANY VALUABLE", n.normalize("Company-2 (ex. Company-1) any () valuable (something)"));
        assertEquals("", n.normalize("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ"));
        assertEquals("1", n.normalize("СПК-1"));
        assertEquals("COMPANY", n.normalize("COMPANY СПК"));
        assertEquals("COMPANY", n.normalize("К(Ф)Х COMPANY"));
        assertEquals("COMPANY", n.normalize(" - -COMPANY--  --"));
        assertEquals("COMPANY", n.normalize("COMPANY Pte."));
        assertEquals("COMPANY", n.normalize("COMPANY Pte.Ltd."));
        assertNull(n.normalize(null));
    }
}

