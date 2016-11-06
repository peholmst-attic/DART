package net.pkhapps.dart.map.importer.xsdenums;

import net.pkhapps.dart.map.database.enums.NlsPlaceNameLanguage;

/**
 * TODO Document me!
 */
public class NlsPlaceNameLanguageMapper extends EnumMapper<NlsPlaceNameLanguage, String> {

    public NlsPlaceNameLanguageMapper() {
        registerEnumConstant(NlsPlaceNameLanguage.fin, "fin");
        registerEnumConstant(NlsPlaceNameLanguage.swe, "swe");
        registerEnumConstant(NlsPlaceNameLanguage.sme, "sme");
        registerEnumConstant(NlsPlaceNameLanguage.smn, "smn");
        registerEnumConstant(NlsPlaceNameLanguage.sms, "sms");
    }
}
