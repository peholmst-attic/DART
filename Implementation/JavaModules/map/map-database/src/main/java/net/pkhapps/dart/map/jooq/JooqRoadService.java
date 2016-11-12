package net.pkhapps.dart.map.jooq;

import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.LocalizedString;
import net.pkhapps.dart.common.location.Municipality;
import net.pkhapps.dart.common.location.Road;
import net.pkhapps.dart.map.api.NameMatch;
import net.pkhapps.dart.map.api.RoadService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.pkhapps.dart.map.database.tables.NlsMunicipality.NLS_MUNICIPALITY;
import static net.pkhapps.dart.map.database.tables.NlsRoad.NLS_ROAD;

/**
 * TODO Document me!
 */
public class JooqRoadService implements RoadService {

    private static final int FETCH_MAX = 500;
    final DSLContext dslContext;

    public JooqRoadService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    private List<Road> findByCondition(@NotNull Condition condition) {
        Field<String> municipalityNameFi = NLS_MUNICIPALITY.NAME_FI.as("municipalityNameFi");
        Field<String> municipalityNameSv = NLS_MUNICIPALITY.NAME_SV.as("municipalityNameSv");

        return dslContext.select(NLS_ROAD.NUMBER,
                NLS_ROAD.NAME_FI,
                NLS_ROAD.NAME_SV,
                NLS_ROAD.LOCATION,
                NLS_ROAD.MIN_ADDRESS_NUMBER_LEFT,
                NLS_ROAD.MIN_ADDRESS_NUMBER_RIGHT,
                NLS_ROAD.MAX_ADDRESS_NUMBER_LEFT,
                NLS_ROAD.MAX_ADDRESS_NUMBER_RIGHT,
                NLS_ROAD.MUNICIPALITY_ID,
                municipalityNameFi,
                municipalityNameSv)
                .from(NLS_ROAD)
                .join(NLS_MUNICIPALITY).on(NLS_MUNICIPALITY.ID.eq(NLS_ROAD.MUNICIPALITY_ID))
                .where(condition)
                .limit(FETCH_MAX)
                .fetch(record -> {
                    LocalizedString name = LocalizedString.builder()
                            .with(Locales.SWEDISH, record.getValue(NLS_ROAD.NAME_SV))
                            .with(Locales.FINNISH, record.getValue(NLS_ROAD.NAME_FI))
                            .build();

                    Municipality municipality = new Municipality(record.getValue(NLS_ROAD.MUNICIPALITY_ID),
                            LocalizedString.builder()
                                    .with(Locales.SWEDISH, record.getValue(municipalityNameSv))
                                    .with(Locales.FINNISH, record.getValue(municipalityNameFi)).build());

                    Integer maxNumber = Math.max(Optional.ofNullable(record.getValue(NLS_ROAD.MAX_ADDRESS_NUMBER_LEFT)).orElse(0),
                            Optional.ofNullable(record.getValue(NLS_ROAD.MAX_ADDRESS_NUMBER_RIGHT)).orElse(0));
                    if (maxNumber == 0) {
                        maxNumber = null;
                    }
                    Integer minNumber = Math.min(Optional.ofNullable(record.getValue(NLS_ROAD.MIN_ADDRESS_NUMBER_LEFT)).orElse(0),
                            Optional.ofNullable(record.getValue(NLS_ROAD.MIN_ADDRESS_NUMBER_RIGHT)).orElse(0));
                    if (minNumber == 0) {
                        minNumber = null;
                    }

                    return new Road(record.getValue(NLS_ROAD.NUMBER), maxNumber, minNumber, name, true, municipality);
                });
    }

    @NotNull
    private Condition getNameMatchCondition(@NotNull String name, @NotNull NameMatch match) {
        switch (Objects.requireNonNull(match)) {
            case CONTAINS:
                return NLS_ROAD.NAME_FI.likeIgnoreCase("%" + name + "%")
                        .or(NLS_ROAD.NAME_SV.likeIgnoreCase("%" + name + "%"));
            case ENDS_WITH:
                return NLS_ROAD.NAME_FI.likeIgnoreCase("%" + name)
                        .or(NLS_ROAD.NAME_SV.likeIgnoreCase("%" + name));
            case EXACT:
                return NLS_ROAD.NAME_FI.equalIgnoreCase(name)
                        .or(NLS_ROAD.NAME_SV.equalIgnoreCase(name));
            case STARTS_WITH:
                return NLS_ROAD.NAME_FI.likeIgnoreCase(name + "%").or(NLS_ROAD.NAME_SV.likeIgnoreCase(name + "%"));
        }
        throw new UnsupportedOperationException("Unsupported match: " + match);
    }

    @Override
    public @NotNull List<Road> findByName(@NotNull Municipality municipality, @Nullable String name, @NotNull NameMatch match) {
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }

        Condition condition = NLS_ROAD.MUNICIPALITY_ID.eq(Objects.requireNonNull(municipality).getId())
                .and(getNameMatchCondition(name, match));

        return findByCondition(condition);
    }

    @Override
    public @NotNull List<Road> findByNameAndAddressNumber(@NotNull Municipality municipality, @Nullable String name,
                                                          @Nullable Integer addressNumber, @NotNull NameMatch match) {
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }

        Condition condition = NLS_ROAD.MUNICIPALITY_ID.eq(Objects.requireNonNull(municipality).getId())
                .and(getNameMatchCondition(name, match));

        if (addressNumber != null) {
            condition = condition.and(NLS_ROAD.MIN_ADDRESS_NUMBER_LEFT.greaterOrEqual(addressNumber).and(NLS_ROAD.MAX_ADDRESS_NUMBER_LEFT.lessOrEqual(addressNumber)))
                    .or(NLS_ROAD.MIN_ADDRESS_NUMBER_RIGHT.greaterOrEqual(addressNumber).and(NLS_ROAD.MAX_ADDRESS_NUMBER_RIGHT.lessOrEqual(addressNumber)));
        }

        return findByCondition(condition);
    }

    @Override
    public @NotNull List<Road> findByNumber(@Nullable Integer number) {
        if (number == null) {
            return Collections.emptyList();
        }

        Condition condition = NLS_ROAD.NUMBER.eq(number);

        return findByCondition(condition);
    }
}
