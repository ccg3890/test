package com.mktback.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mktback.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spec.class);
        Spec spec1 = new Spec();
        spec1.setId("id1");
        Spec spec2 = new Spec();
        spec2.setId(spec1.getId());
        assertThat(spec1).isEqualTo(spec2);
        spec2.setId("id2");
        assertThat(spec1).isNotEqualTo(spec2);
        spec1.setId(null);
        assertThat(spec1).isNotEqualTo(spec2);
    }
}
