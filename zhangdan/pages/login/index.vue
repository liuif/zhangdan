<template>
	<view>
		<view class="container">
			<view class="input-container">
				<input class="input-field" type="text" placeholder="邮箱" v-model="email" />
				<input class="input-field" type="password" placeholder="密码" v-model="password" />
				<button class="login-button" @click="login">登录</button>
			</view>
		</view>
		<router-link class="register" to="/pages/login/register">
			<text class="word">没有账号？立即注册！</text>
		</router-link>
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
	.word{
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
	import { mapActions } from 'vuex';
	
	export default {
		data() {
			return {
				email: "",
				password: "",
			}
		},
		methods: {
			//...mapActions(['setUser']),
			login() {
				const data = {
					email: this.email,
					password: this.password
				};

				uni.request({
					url: './api/login',
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
						console.log(res[1].data);
						console.log(res);
						console.log(result);
						return;
					}
					if (result.code === 0) {
						// 登录成功
						uni.showToast({
							title: '登录成功',
							icon: 'success'
						});
						
						//this.setUser(result.data);
						// 将用户信息存储在本地，以备后续使用
						uni.setStorageSync('name', result.data.username);
						uni.setStorageSync('email', result.data.email);
						uni.setStorageSync('password', result.data.password);

						// 登录成功，跳转至主页
						uni.switchTab({
							url: '/pages/tabbar/tabbar-1/tabbar-1'
						});
					} else {
						// 登录失败，显示错误消息
						uni.showToast({
							title: result.msg,
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