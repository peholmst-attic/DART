package net.pkhapps.dart.map.jooq;

import net.pkhapps.dart.common.Coordinates;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.LocalizedString;
import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.map.api.MunicipalityService;
import net.pkhapps.dart.map.api.NameMatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.pkhapps.dart.map.database.tables.NlsMunicipality.NLS_MUNICIPALITY;

/**
 * TODO Document me!
 */
public class JooqMunicipalityService implements MunicipalityService {

    private static final int FETCH_MAX = 100;
    private final DSLContext dslContext;

    public JooqMunicipalityService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull List<Municipality> findByName(@Nullable String name, @NotNull NameMatch match) {
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }
        return dslContext.select(NLS_MUNICIPALITY.ID, NLS_MUNICIPALITY.NAME_FI, NLS_MUNICIPALITY.NAME_SV)
                .from(NLS_MUNICIPALITY)
                .where(NameMatchUtils.getNameMatchCondition(name, match, NLS_MUNICIPALITY.NAME_FI, NLS_MUNICIPALITY.NAME_SV))
                .limit(FETCH_MAX)
                .fetch(record -> new Municipality(record.getValue(NLS_MUNICIPALITY.ID),
                        LocalizedString.builder()
                                .with(Locales.SWEDISH, record.getValue(NLS_MUNICIPALITY.NAME_SV))
                                .with(Locales.FINNISH, record.getValue(NLS_MUNICIPALITY.NAME_FI))
                                .build()));
    }

    @Override
    public @NotNull Optional<Municipality> findByCoordinates(@Nullable Coordinates coordinates) {
        // TODO Implement me!
        throw new UnsupportedOperationException("This operation is not yet supported because the required data is missing from the database");
    }
}
