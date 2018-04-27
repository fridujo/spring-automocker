package com.github.fridujo.sample;

import com.github.fridujo.automocker.api.ResetMocks;
import com.github.fridujo.automocker.base.Automocker;
import com.github.fridujo.sample.customer.CustomerRepository;
import com.github.fridujo.sample.order.OrderRepository;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Automocker
@ResetMocks(disable = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcApplication.class)
class JdbcApplicationTest {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private Map<String, DataSource> datasources;

    @SuppressWarnings("unchecked")
    private static List<String> listTables(DataSource dataSource)
        throws MetaDataAccessException, SQLException {
        return (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, dbmd -> {
            ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                names.add(rs.getString(3));
            }
            return names;
        });
    }

    @Test
    public void data_initializer_have_already_persisted_customer() {
        assertThat(customerRepo.findAll()).hasSize(1)
            .extracting("firstname", "lastname")
            .contains(new Tuple("Dave", "Matthews"));
    }

    @Test
    public void data_initializer_have_already_persisted_orders() {
        assertThat(orderRepo.findAll()).hasSize(2);
        assertThat(Sets.newHashSet(orderRepo.findAll())
            .stream()
            .flatMap(o -> o.getLineItems()
                .stream())
            .collect(Collectors.toList())).extracting("description")
            .contains("Fender Jag-Stang Guitar", "Gene Simmons Axe Bass");
    }

    @Test
    public void datasources_are_different_and_data_is_not_shared()
        throws MetaDataAccessException, SQLException {
        assertThat(datasources).containsOnlyKeys("customerDataSource", "orderDataSource");

        assertThat(listTables(datasources.get("customerDataSource")))
            .as("Tables of customer database")
            .containsExactlyInAnyOrder("CUSTOMER");

        assertThat(listTables(datasources.get("orderDataSource")))
            .as("Tables of order database")
            .containsExactlyInAnyOrder("SAMPLEORDER", "LINEITEM", "SAMPLEORDER_LINEITEM");
    }
}
