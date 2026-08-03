const path = require("path");

let addon;
try {
  addon = require("./build/Release/iris_node_addon.node");
} catch (e) {
  console.warn(
    "[NodeAddon] Native addon binary not compiled yet. Call node-gyp rebuild to compile.",
  );
  addon = {
    initializeEngine: () => true,
  };
}

module.exports = addon;
