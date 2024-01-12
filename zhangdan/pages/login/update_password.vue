<template>
	<view>
		<view class="container">
			<view class="input-container">
				<input class="input-field" type="password" placeholder="原密码" v-model="pass" />
				<input class="input-field" type="password" placeholder="新密码" v-model="password" />
				<input class="input-field" type="password" placeholder="重复新密码" v-model="password2" />
				<button class="login-button" @click="update_password">修改密码</button>
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
				user: "",
				password: "",
				password2: "",
			}
		},
		methods: {
			update_password() {
				if (this.password === this.password2) {
					if (uni.getStorageSync('password') == this.pass) {
						const data = {
							email: uni.getStorageSync('email'),
							user: uni.getStorageSync('name'),
							password: this.password,
						};

						uni.request({
							url: '/api/update_password',
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
									title: '修改密码成功',
									icon: 'success'
								});

								// 登录成功，跳转至主页
								uni.switchTab({
									url: '/pages/tabbar/tabbar-5/tabbar-5'
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
					} else {
						uni.showToast({
							title: "原密码错误！",
							icon: 'none'
						});
					}
				} else {
					uni.showToast({
						title: "两次密码不一致！",
						icon: 'none'
					});
				}
			}
		}
	}
</script>