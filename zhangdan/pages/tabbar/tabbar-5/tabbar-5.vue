<template>
	<view class="person-center">
		<view class="avatar">
			<image class="avatar-image" :src="avatarUrl"></image>
		</view>
		<view class="username">
			<text class="username-text">{{ username }}</text>
		</view>
		<router-link class="menu-item" to="/pages/tabbar/tabbar-2/tabbar-2">
			<text>账本管理</text>
		</router-link>
		<router-link class="menu-item" to="/pages/classify/expensetype">
			<text>分类管理</text>
		</router-link>
		<router-link class="menu-item" to="/pages/tabbar/tabbar-4/tabbar-4">
			<text>账单报告</text>
		</router-link>
		<router-link class="menu-item" to="/pages/budget/ledgerBudget/ledgerBudget">
			<text>预算设置</text>
		</router-link>
		<router-link class="menu-item" to="/pages/property/property">
			<text>我的资产</text>
		</router-link>
		<router-link class="menu-item" to="/pages/share/share">
			<text>共享申请</text>
		</router-link>
		<router-link class="menu-item" to="/pages/login/update_password">
			<text>修改密码</text>
		</router-link>
		<view class="asset-info">
			<text>总资产 </text>
			<text class="asset-num">{{ totalAsset }}</text>
		</view>
		<view class="asset-info">
			<text>总负债 </text>
			<text class="asset-num">{{ totalDebt }}</text>
		</view>
		<view class="asset-info">
			<text>净资产 </text>
			<text class="asset-num">{{ totalAsset - totalDebt }}</text>
		</view>
		<router-link class="bar" to="/pages/login/index">
			<text>退出登录</text>
		</router-link>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				avatarUrl: '/static/img/touxiang.jpg',
				totalAsset: "1000",
				totalDebt: "100",
				username: uni.getStorageSync('name')
			}
		},
		methods: {
			getTotalBalance() {
				uni.request({
					url: '/api/getTotalBalance',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email'),
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.totalAsset = res.data.data;
						} else {
							uni.showToast({
								title: '获取资金，请稍后重试',
								icon: 'none'
							})
						}
					},
					fail: () => {
						uni.showToast({
							title: '网络异常，请稍后重试',
							icon: 'none'
						})
					}
				})
			}
		 },
		mounted() {
			this.getTotalBalance()
		},
	}
</script>

<style>
	.person-center {
		padding: 50rpx;
		background-color: #f5f5f5;
		/* 添加背景颜色 */
		text-align: center;
		/* 居中文本 */
	}

	.avatar {
		margin-left: 43%;
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		overflow: hidden;
		margin-bottom: 15rpx;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.2);
	}

	.avatar-image {
		width: 100%;
		height: 100%;
		object-fit: cover;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 32rpx;
		margin-bottom: 20rpx;
		text-align: center;
	}

	.username {
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 20rpx;
		color: #333;
		/* 修改文本颜色 */
	}

	.menu-item {
		padding: 10rpx;
		font-size: 28rpx;
		border-bottom: 1px solid #eee;
		/* 保留菜单项的下划线样式 */
		text-decoration: none;
		/* 移除文本的下划线 */
		display: flex;
		align-items: center;
		color: #666;
	}

	.menu-item i {
		margin-right: 10rpx;
		/* 调整图标和文本之间的间距 */
	}

	.asset-info {
		margin-top: 20rpx;
		display: flex;
		justify-content: space-between;
		font-size: 28rpx;
		color: #666;
		/* 修改信息颜色 */
	}

	.asset-num {
		font-size: 30rpx;
		color: #333;
		/* 修改数字颜色 */
	}

	.bar {
		width: 200rpx; // 设置条形框的宽度
		height: 20rpx; // 设置条形框的高度
		border: 1rpx solid #666; // 设置条形框的边框样式和颜色
		margin: 50rpx auto; // 居中对齐条形框，并设置上下的间距
		text-decoration: none;
		color: #333;
	}
</style>