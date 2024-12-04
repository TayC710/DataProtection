const path = require("path")

module.exports = {
    entry: './src/main/webapp/js/utils.js',
    output: {
        path: path.resolve(__dirname, "src/main/webapp/dist"),
        filename: "utils.js"
    },
    module: {
        rules: [
            {
                test: /\.([cm]?ts|tsx)$/,
                loader: "ts-loader"},
            {
                test: /\.m?js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            }
        ],
    },
    mode: "production"
}