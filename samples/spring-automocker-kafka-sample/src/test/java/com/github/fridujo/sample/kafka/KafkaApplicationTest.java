package com.github.fridujo.sample.kafka;

import com.github.fridujo.automocker.api.ResetMocks;
import com.github.fridujo.automocker.base.Automocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Automocker
@ResetMocks(disable = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KafkaApplication.class)
class KafkaApplicationTest {

    @Test
    void name() {
    }
}
