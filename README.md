![BlinkLoad icon](docs/assets/icon/icon_128x128.png)

# BlinkLoad

Minecraft mod that caches assets to reduce game loading times.

## Benchmark

### Specifications

- CPU: Intel i7-9750H @ 3.1GHz
- 16GB RAM @ 2667MHz

### Mods

- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Architectury API](https://modrinth.com/mod/architectury-api)
- [Mod Menu](https://modrinth.com/mod/modmenu)

### Results

![Benchmark with Blinkload](docs/benchmarks/benchmark_with_blinkload.png)
![Benchmark without BlinkLoad](docs/benchmarks/benchmark_without_blinkload.png)

Results may vary based on hardware (e.x. faster hardware may benefit less).

## Dependencies

### Required

- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Architectury API](https://modrinth.com/mod/architectury-api)

## Compatibility info

### Compatible mods

BlinkLoad should be compatible with most, if not all, of the popular optimisation mods currently on Modrinth/CurseForge for Minecraft
`1.20.x`.

### Incompatibilities

See
the [issue tracker](https://github.com/steves-underwater-paradise/blinkload/issues?q=is%3Aissue+is%3Aopen+sort%3Aupdated-desc+label%3Acompatibility)
for a list of incompatibilities.

## Download

[![GitHub](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/cozy/available/github_vector.svg)](https://github.com/steves-underwater-paradise/blinkload)
[![Modrinth](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/blinkload)
[![CurseForge](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/blinkload)

![Fabric](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/compact/supported/fabric_vector.svg)
![Quilt](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/compact/supported/quilt_vector.svg)
![Forge](https://github.com/intergrav/devins-badges/raw/2dc967fc44dc73850eee42c133a55c8ffc5e30cb/assets/compact/supported/forge_vector.svg)
![NeoForge](docs/assets/badges/compact/supported/neoforge_vector.svg)

See the version info in the filename for the supported Minecraft versions.  
Made for the Fabric, Quilt, Forge, and NeoForge modloaders.  
Client-side and server-side.

## FAQ

- Q: Will you be backporting this mod to lower Minecraft versions?  
  A: No.

- Q: Does this mod work in multiplayer?  
  A: Yes.

- Q: Does only the client need this mod or does the server need it too?  
  A: Only the client needs this mod.

## Contributing

If you've encountered a problem or you want to suggest
features, [create an issue](https://github.com/steves-underwater-paradise/blinkload/issues/new) on the issue tracker.

### Development

- `git clone https://github.com/steves-underwater-paradise/blinkload.git`
- `cd blinkload`
- `./gradlew build`

## License

This project is licensed under LGPLv3, see [LICENSE](https://github.com/steves-underwater-paradise/blinkload/blob/1.20.1/LICENSE).
