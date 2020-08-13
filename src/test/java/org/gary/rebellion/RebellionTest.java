package org.gary.rebellion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RebellionTest {

    private final int rank;
    private final int supporters;
    private final int focusCheck;
    private final int secondaryCheck;
    private final int rebellionActions;
    private final int maxTeams;

    public RebellionTest(int rank, int supporters, int focusCheck, int secondaryCheck, int rebellionActions, int maxTeams){
        this.rank = rank;
        this.supporters = supporters;
        this.focusCheck = focusCheck;
        this.secondaryCheck = secondaryCheck;
        this.rebellionActions = rebellionActions;
        this.maxTeams = maxTeams;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> entries(){
        return Arrays.asList(new Object[][]{
                {1,9,2,0,1,2},
                {2,14,3,0,2,2},
                {3,19,3,1,2,3},
                {4,29,4,1,2,3},
                {5,39,4,1,2,4},
                {6,54,5,2,2,4},
                {7,74,5,2,3,4},
                {8,104,6,2,3,5},
                {9,159,6,3,3,5},
                {10,234,7,3,3,5},
                {11,329,7,3,4,6},
                {12,474,8,4,4,6},
                {13,664,8,4,4,6},
                {14,954,9,4,4,6},
                {15,1349,9,5,5,7},
                {16,1899,10,5,5,7},
                {17,2699,10,5,5,7},
                {18,3849,11,6,5,7},
                {19,5349,11,6,6,7},
                {20,5350,12,6,6,8},
        });
    }

    @Test
    public void testRank(){
        Rebellion rebellion = new Rebellion();
        rebellion.setSupporters(supporters);
        rebellion.setMaxRank(20);
        assertThat(rebellion.getRebellionRank()).isEqualTo(rank);
    }

    @Test
    public void testFocusCheck(){
        Rebellion rebellion = new Rebellion();
        rebellion.setSupporters(supporters);
        rebellion.setMaxRank(20);
        assertThat(rebellion.getFocusCheckBonus()).isEqualTo(focusCheck);
    }

    @Test
    public void testSecondaryCheck(){
        Rebellion rebellion = new Rebellion();
        rebellion.setSupporters(supporters);
        rebellion.setMaxRank(20);
        assertThat(rebellion.getSecondaryCheckBonus()).isEqualTo(secondaryCheck);
    }

    @Test
    public void testRebellionActions(){
        Rebellion rebellion = new Rebellion();
        rebellion.setSupporters(supporters);
        rebellion.setMaxRank(20);
        assertThat(rebellion.getActions()).isEqualTo(rebellionActions);
    }


    @Test
    public void testMaxTeams(){
        Rebellion rebellion = new Rebellion();
        rebellion.setSupporters(supporters);
        rebellion.setMaxRank(20);
        assertThat(rebellion.getMaxTeams()).isEqualTo(maxTeams);
    }


}
