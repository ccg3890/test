package com.mktback.application.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mktback.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecDTO.class);
        SpecDTO specDTO1 = new SpecDTO();
        specDTO1.setId("id1");
        SpecDTO specDTO2 = new SpecDTO();
        assertThat(specDTO1).isNotEqualTo(specDTO2);
        specDTO2.setId(specDTO1.getId());
        assertThat(specDTO1).isEqualTo(specDTO2);
        specDTO2.setId("id2");
        assertThat(specDTO1).isNotEqualTo(specDTO2);
        specDTO1.setId(null);
        assertThat(specDTO1).isNotEqualTo(specDTO2);
    }
}
