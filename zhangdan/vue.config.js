module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 设置目标域名
        changeOrigin: true, // 开启跨域
        pathRewrite: {
          '^/api': '/kszd_Web_exploded/' // 将请求路径中的 '/api' 替换为空字符串
        }
      }
    }
  }
}