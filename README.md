# nbody
An N-body simulation and visualization application.

## Motivation
This minor project was undertaken simply because deterministic and chaotic evolutions of complex systems such as physics simulations and similar fields such as cellular automata have always piqued my interest.

![Simulation with a large number of bodies](docs/large-sim.png =500x)

## Math, Physics, and Algorithms
This program simulates the n-body problem in three dimensions for a system of gravitational bodies. It can take an arbitrary number of bodies (position and velocity) and simulate their gravitational effect on each other over time.

![Simulation of the inner solar system](docs/solar-system.png =500x)

The default integration algorithm is Euler's method with a configurable step size. A basic framework already exists with the intention of implementing other numerical methods such as exponential integrators and the Runge-Kutta family of methods.

The default simulation algorithm is a direct brute-force calculation of the gravitational forces between bodies O(n^2). The other currently supported algorithm is the Barnes-Hut simulation which uses a recursive octree to reduce the computations necessary for far away bodies to achieve O(n log n).

![Barnes-Hut Simulation in 2D](docs/barnes-hut-2d.png =500x)

A basic framework exists to extend to other simulation methods such as the particle mesh method in the future.

![Barnes-Hut Simulation in 3D](docs/barnes-hut-3d.png =500x)

## The Code
This project is in Java because I already knew it well enough to be able to rapidly produce a working prototype and focus on understanding and implementing the different mathematics.

Rather than use Java3D's graphics engine, I opted to use Java's native 2D graphics as it afforded me an opportunity to refresh my knowledge of vector math and review the basics 3D to 2D projection and other simple computer graphics.

## The Future
This simulation does not include the effects of dark matter, which is crucial for simulating and understanding large (galactic) sized systems.

Utlimately, should this project expand, it will likely be broken up and rewritten into two parts for the simulation engine and the visualizer. This will allow me to use a language more conducive to high-performance and highly-parallel computation such as CUDA/OpenCL as the current implementation is single threaded and CPU only.

## License
This project is released under the MIT License.

