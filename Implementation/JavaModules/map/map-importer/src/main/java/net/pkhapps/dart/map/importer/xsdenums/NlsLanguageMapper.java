package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsLanguage;

/**
 * TODO Document me!
 */
public class NlsLanguageMapper extends EnumMapper<NlsLanguage, String> {

    public NlsLanguageMapper() {
        registerEnumConstant(NlsLanguage.fin, "fin");
        registerEnumConstant(NlsLanguage.swe, "swe");
        registerEnumConstant(NlsLanguage.sme, "sme");
        registerEnumConstant(NlsLanguage.smn, "smn");
        registerEnumConstant(NlsLanguage.sms, "sms");
    }
}
