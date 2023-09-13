package ua.gov.odisey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ua.gov.odisey.web.rest.TestUtil;

class DodumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dodument.class);
        Dodument dodument1 = new Dodument();
        dodument1.setId(1L);
        Dodument dodument2 = new Dodument();
        dodument2.setId(dodument1.getId());
        assertThat(dodument1).isEqualTo(dodument2);
        dodument2.setId(2L);
        assertThat(dodument1).isNotEqualTo(dodument2);
        dodument1.setId(null);
        assertThat(dodument1).isNotEqualTo(dodument2);
    }
}
