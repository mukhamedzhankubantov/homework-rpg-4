package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        // TODO: Validate inputs (null checks, alive checks, required skills).
        // TODO: Implement round-based simulation:
        // 1) Team A casts on Team B
        // 2) Team B casts on Team A (if still alive)
        // 3) Track rounds and log each step
        // 4) Stop when one team is defeated (or max rounds reached)
        //
        // Optional extension:
        // Use random for critical strikes or other deterministic events.
        // Example: boolean critA = random.nextInt(100) < 10;
        RaidResult result = new RaidResult();
        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setWinner("Invalid");
            result.setRounds(0);
            result.addLine("Raid could not start: null input provided.");
            return result;
        }
        if (!teamA.isAlive() || !teamB.isAlive()) {
            result.setWinner("Invalid");
            result.setRounds(0);
            result.addLine("Raid could not start: one or both teams are already defeated.");
            return result;
        }
        final int maxRounds = 100;
        int round = 0;

        result.addLine("Raid started: " + teamA.getName() + " vs " + teamB.getName());
        result.addLine(teamA.getName() + " uses " + teamASkill.getSkillName() + " (" + teamASkill.getEffectName() + ")");
        result.addLine(teamB.getName() + " uses " + teamBSkill.getSkillName() + " (" + teamBSkill.getEffectName() + ")");

        while (teamA.isAlive() && teamB.isAlive() && round < maxRounds) {
            round++;
            result.addLine("");
            result.addLine("Round " + round + ":");
            int beforeHealthB = teamB.getHealth();
            teamASkill.cast(teamB);
            int damageByA = Math.max(0, beforeHealthB - teamB.getHealth());
            result.addLine(teamA.getName() + " attacks " + teamB.getName()
                    + " with " + teamASkill.getSkillName() + " [" + teamASkill.getEffectName() + "]"
                    + " for " + damageByA + " damage.");

            result.addLine(teamB.getName() + " status: HP=" + teamB.getHealth() + ", ATK=" + teamB.getAttackPower());

            if (!teamB.isAlive()) {
                break;
            }

            int beforeHealthA = teamA.getHealth();
            teamBSkill.cast(teamA);
            int damageByB = Math.max(0, beforeHealthA - teamA.getHealth());
            result.addLine(teamB.getName() + " attacks " + teamA.getName()
                    + " with " + teamBSkill.getSkillName() + " [" + teamBSkill.getEffectName() + "]"
                    + " for " + damageByB + " damage.");
            result.addLine(teamA.getName() + " status: HP=" + teamA.getHealth() + ", ATK=" + teamA.getAttackPower());
        }


//        result.setRounds(0);
        result.setRounds(round);
//        result.setWinner("TBD");
//        result.addLine("TODO: implement raid simulation");
        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
            result.addLine("");
            result.addLine("Winner: " + teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
            result.addLine("");
            result.addLine("Winner: " + teamB.getName());
        } else if (!teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner("Draw");
            result.addLine("");
            result.addLine("Battle ended in a draw.");
        } else {
            result.setWinner("Draw");
            result.addLine("");
            result.addLine("Battle stopped after reaching max rounds.");
        }
        return result;
    }
}
