package ca.ulaval.glo4003.infrasctructure.config;

import java.time.Duration;

public class SimulationSettings {
  public static final Duration CLOCK_TICK_DURATION = Duration.ofMinutes(30);
  public static final Duration SIMULATION_UPDATE_FREQUENCY = Duration.ofSeconds(10);
}
