package net.pkhapps.dart.common.jooq;

import net.pkhapps.dart.common.Raster;
import org.apache.commons.codec.binary.Hex;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;

import static org.jooq.tools.Convert.convert;

/**
 * TODO Document me!
 */
public class RasterBinding implements Binding<Object, Raster> {

    @Override
    public Converter<Object, Raster> converter() {
        return new RasterConverter();
    }

    @Override
    public void sql(BindingSQLContext<Raster> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::raster");
    }

    @Override
    public void register(BindingRegisterContext<Raster> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<Raster> ctx) throws SQLException {
        byte[] bytes = convert(ctx.convert(converter()).value(), byte[].class);
        // Feels stupid to convert this to a HEX string, but I don't know how to insert it as binary without
        // PostGIS complaining
        String hexString = Hex.encodeHexString(bytes);
        ctx.statement().setString(ctx.index(), hexString);
    }

    @Override
    public void set(BindingSetSQLOutputContext<Raster> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetResultSetContext<Raster> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getBytes(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Raster> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getBytes(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Raster> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
