module.exports = {
    devServer: {
      proxy: {
        '^/pas': {
          target: 'http://localhost:8080'
        }
      }
    }
  }