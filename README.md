# Cat Lab

[![Build and test](https://github.com/Parzival-05/comp-graph.lab1/actions/workflows/ktlint.yml/badge.svg)](https://github.com/Parzival-05/comp-graph.lab1/actions/workflows/ktlint.yml)
[![Ktlint](https://github.com/Parzival-05/comp-graph.lab1/actions/workflows/build_and_test.yml/badge.svg)](https://github.com/Parzival-05/comp-graph.lab1/actions/workflows/build_and_test.yml)

This project simulates and visualizes the behavior of cats on a rectangular map, taking into account their interaction
at a distance.

- During the time TAU, the cat moves to a random point in the neighborhood of the previous position
- If two cats are at a distance not exceeding _r₀_, they try to start a **fight** with a probability of _1_
- If two cats are at a distance _R₀ > r₀_, they begin to **hiss** with a probability inversely proportional to the
  square of the distance between them
- Otherwise, the cat remains **calm**

## Usage

To run the application, clone this repository and run the command below:

```
./gradlew run
```

## Features

1. [x] Logs
2. [x] Metrics
3. [x] Cats can leave the map and spawn on the border.

## Parameters

| Parameter           | Code Name        | Restrictions                            |
|---------------------|------------------|-----------------------------------------|
| Cat Count           | `PARTICLE_COUNT` | < 5\*10^5                               |
| Tau Update Interval | `TAU`            | \> 500 ms                               |
| Metric              | `metric`         | `euclidean`, `greatCircle`, `manhattan` |

## Demonstration

| Visualization                                                                                           |
|---------------------------------------------------------------------------------------------------------|
| **PARTICLE_COUNT = 50, TAU = 100**                                                                      |
| <img src="./images/50_100L.gif" width="700" alt="Simulation with PARTICLE_COUNT 50 and TAU 100L">       |
| **PARTICLE_COUNT = 50000, TAU = 500**                                                                   |
| <img src="./images/50000_500L.gif" width="700" alt="Simulation with PARTICLE_COUNT 50000 and TAU 500L"> |

## Task distribution

| **Name**        | **Tasks**                     |
|-----------------|-------------------------------|
| David Akhmedov  | Algorithm + Architecture + CI |
| Anna Ermolovich | UI + README.md                |
| Danil Parfenov  | Logging + tests               |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
