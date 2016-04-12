package net.pkhapps.dart.types;

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;

public class CoordinatesBinding implements Binding<Object, Coordinates> {

    //ST_SetSRID(ST_Point(22.301513, 60.298260),4326)
    //ST_Astext(location) -> "POINT(22.301513 60.29826)"  22E (lon) 60N (lat)

    @Override
    public Converter<Object, Coordinates> converter() {
        return new CoordinatesConverter();
    }

    @Override
    public void sql(BindingSQLContext<Coordinates> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value()));
    }

    @Override
    public void register(BindingRegisterContext<Coordinates> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<Coordinates> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void set(BindingSetSQLOutputContext<Coordinates> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetResultSetContext<Coordinates> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Coordinates> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Coordinates> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
