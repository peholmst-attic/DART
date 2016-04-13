package net.pkhapps.dart.types;

import net.pkhapps.dart.db.tables.Stations;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static net.pkhapps.dart.db.tables.Stations.STATIONS;

/**
 * Created by petterprivate on 11/04/16.
 */
public class TestApp {

    public static void main(String[] args) throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql:dart", "dart", "dart")) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES_9_4);
            Coordinates coordinates = create.select(STATIONS.LOCATION).from(STATIONS).fetchAny(STATIONS.LOCATION);
            System.out.println(coordinates);
            create.insertInto(STATIONS).set(STATIONS.LOCATION, coordinates).set(STATIONS.NAME_FI, "test_fi")
                    .set(STATIONS.NAME_SV, "test_sv").set(STATIONS.MUNICIPALITY_ID, 1L).execute();
        }
    }
}
