// vue.config.js
module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      "^/api": {
        target: "http://localhost:8082",
        changeOrigin: true,
        pathRewrite: { "^/api": "" }, // убирает /api из пути
      },
    },
  },
};
