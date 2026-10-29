{ pkgs ? import <nixpkgs> {} }:
# Dev shell for running this plugin against a local RuneLite dev build.
# `nix-shell --run "./gradlew run"` launches the modified client (resolved from mavenLocal)
# with this plugin loaded via ExternalPluginManager.loadBuiltin.
pkgs.mkShell {
  buildInputs = with pkgs; [ jdk11 git libglvnd ];
  # RuneLite's GPU plugin (librlawt.so) dlopens libGL.so.1; NixOS isn't FHS so point at the
  # GLVND dispatch lib + the system vendor driver, else the GPU plugin fails to start.
  shellHook = ''
    export JAVA_HOME=${pkgs.jdk11}/lib/openjdk
    export LD_LIBRARY_PATH=${pkgs.libglvnd}/lib:/run/opengl-driver/lib''${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}
  '';
}
