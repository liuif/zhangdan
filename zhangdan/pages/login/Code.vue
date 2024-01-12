<template>
	<view>
		<view class="container">
			<view class="input-container">
				<text>已发送验证码至您的邮箱</text>
				<text>请输入验证码：</text>
				<input class="input-field" type="text" placeholder="验证码" v-model="code" />
				<button class="login-button" @click="testCode">提交</button>
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
				code: "",
			}
		},
		methods: {
			testCode() {
				console.log(uni.getStorageSync('code'));
				if (this.code === uni.getStorageSync('code')) {
					uni.showToast({
						title: '验证成功',
						icon: 'success'
					});

					// 登录成功，跳转至主页
					uni.navigateTo({
						url: '/pages/login/newpassword'
					});
				} else {
					uni.showToast({
						title: '验证码错误',
						icon: 'none'
					});
				}
			}
		}
	}
</script>