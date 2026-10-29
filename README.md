# Bank Equipment Preview

RuneLite plugin showing currently equipped items beside the bank. Right-click **Show worn items** and choose **Toggle equipment preview** to show or hide it.

Hold **Alt** and drag the preview anywhere on the game canvas; RuneLite remembers its position.

## Build and test locally

Install JDK 11, then run:

```sh
./gradlew build
./gradlew run
```

On NixOS, use `nix-shell --run './gradlew build'` or `nix-shell --run './gradlew run'`.

The run task launches RuneLite in developer mode using your normal RuneLite home directory. For Jagex accounts, follow RuneLite's [local development login instructions](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts).
