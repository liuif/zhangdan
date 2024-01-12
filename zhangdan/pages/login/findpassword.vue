<template>
	<view>
		<view class="container">
			<view class="input-container">
				<input class="input-field" type="text" placeholder="邮箱" v-model="email" />
				<button class="login-button" @click="findpassword">找回密码</button>
			</view>
		</view>
	</view>
</template>

<style>
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 70vh;
		background-color: white;
		color: black;
	}

	.input-container {
		width: 300px;
		height: 200px;
		border-radius: 15px;
		display: flex;
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background-color: #4d7df9;
		margin-top: 20px;
	}

	.input-field {
		width: 250px;
		height: 30px;
		margin-bottom: 10px;
		padding: 5px;
		border-radius: 5px;
		border: none;
		background-color: white;
	}

	.login-button {
		width: 150px;
		height: 35px;
		background-color: white;
		color: #4d7df9;
		border: none;
		border-radius: 5px;
		font-weight: bold;
	}

	.login-button:hover {
		background-color: white;
		cursor: pointer;
	}

	.register {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 100px;
		background-color: white;
		position: relative;
		text-decoration: none;
	}

	.word {
		display: flex;
		justify-content: center;
		color: gray;
		text-decoration: none;
	}

	.word:hover {
		color: black;
		cursor: pointer;
	}
</style>

<script>
	export default {
		data() {
			return {
				email: "",
				code: "",
			}
		},
		methods: {
			generateRandomCode() {
				let code = '';
				for (let i = 0; i < 6; i++) {
					// 生成一个0到9的随机整数
					let randomNum = Math.floor(Math.random() * 10);
					// 将随机整数转为字符串拼接到验证码上
					code += randomNum.toString();
				}
				return code;
			},
			findpassword() {
				const data = {
					email: this.email,
					code: this.generateRandomCode(),
				};
				console.log(data.email);
				console.log(this.code);
				uni.setStorageSync('email', this.email);
				uni.setStorageSync('code', data.code);
				uni.request({
					url: '/api/findpassword',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data
				}).then((res) => {
					const result = res[1].data;
					console.log(res[1].data);
					if (!result) {
						console.error('登录接口返回数据为空');
						return;
					}
					if (result.code === 0) {
						// 登录成功
						uni.showToast({
							title: '已发送邮件',
							icon: 'success'
						});

						// 登录成功，跳转至Code
						uni.navigateTo({
							url: '/pages/login/Code'
						});
					} else {
						// 登录失败，显示错误消息
						uni.showToast({
							title: "请输入正确的邮箱号",
							icon: 'none'
						});
					}
				}).catch((error) => {
					console.log('请求失败', error);
				});
			}
		}
	}
</script>