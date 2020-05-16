package rebellion;

import core.PCGVarLoader;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class PCGVarLoaderTest {

    @Test
    public void basicTest(){
        PCGVarLoader pcgVarLoader = new PCGVarLoader(new File("src/test/resources/generic.xml"));

        assertThat(pcgVarLoader.getVars().get("bab")).isEqualTo("4");
        assertThat(pcgVarLoader.getVars().get("str")).isEqualTo("0");
        assertThat(pcgVarLoader.getVars().get("dex")).isEqualTo("4");
        assertThat(pcgVarLoader.getVars().get("con")).isEqualTo("0");
        assertThat(pcgVarLoader.getVars().get("int")).isEqualTo("4");
        assertThat(pcgVarLoader.getVars().get("wis")).isEqualTo("0");
        assertThat(pcgVarLoader.getVars().get("cha")).isEqualTo("-1");
        assertThat(pcgVarLoader.getVars().get("init")).isEqualTo("4");
        assertThat(pcgVarLoader.getVars().get("bluff")).isEqualTo("4");
        assertThat(pcgVarLoader.getVars().get("fort")).isEqualTo("4");
    }
}
