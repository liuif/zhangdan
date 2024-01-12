<template>
	<view class="content">
		<view class="t-task">
			<view class="t-task-item" v-for="user in users" :key="user">
				<view class="left">
					<view class="taskname">{{ user.email }}</view>
					<view class="taskdesc">{{ user.ledgername }}</view>
				</view>
				<view class="right" style="width: 50px;" hover-class="t-hc" @click="pass(user.id)">
					同意
				</view>
				<view class="right" style="width: 50px;" hover-class="t-hc" @click="refuse(user.id)">
					拒绝
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				users: [], // 账本列表
				useremail: uni.getStorageSync('email')
			};
		},
		methods: {
			getapplicant() {
				uni.request({
					url: '/api/getapplicant',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email'),
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.users = res.data.users;
						} else {
							uni.showToast({
								title: '获取申请列表失败，请稍后重试',
								icon: 'none'
							});
						}
					},
					fail: () => {
						uni.showToast({
							title: '网络异常，请稍后重试',
							icon: 'none'
						});
					}
				});
			},
			pass(id) {
				uni.showModal({
					title: '提示',
					content: '确认通过该用户吗？',
					success: (res) => {
						uni.request({
							url: '/api/updateSharedLedger',
							method: 'POST',
							data: {
								id: id,
								code: 1,
							},
							success: (res) => {
								console.log(res);
								if (res.statusCode === 200) {
									this.getapplicant();
								} else {
									uni.showToast({
										title: '通过失败，请稍后重试',
										icon: 'none'
									});
								}
							},
							fail: () => {
								uni.showToast({
									title: '网络异常，请稍后重试',
									icon: 'none'
								});
							}
						});
					},
				});
			},
			refuse(id) {
				uni.showModal({
					title: '提示',
					content: '确认拒绝该用户吗？',
					success: (res) => {
						uni.request({
							url: '/api/updateSharedLedger',
							method: 'POST',
							data: {
								id: id,
								code: 0,
							},
							success: (res) => {
								console.log(res);
								if (res.statusCode === 200) {
									this.getapplicant();
								} else {
									uni.showToast({
										title: '拒绝失败，请稍后重试',
										icon: 'none'
									});
								}
							},
							fail: () => {
								uni.showToast({
									title: '网络异常，请稍后重试',
									icon: 'none'
								});
							}
						});
					}
				})
			},
		},
		mounted() {
			this.getapplicant()
		}
	}
</script>

<style scoped lang="scss">
	.content {
		box-sizing: border-box;
		padding-bottom: 20rpx;
		background: #fff;
		min-width: 100vw;
		// min-height: 100vh;
	}

	.t-hc {
		opacity: 0.7;
	}

	.t-task {
		box-sizing: border-box;
		background: #fff;
		padding: 0rpx 30rpx;

		.t-title {
			font-size: 32rpx;
			color: #000;
			text-align: center;
		}

		.t-task-item {
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;
			height: 80px;
			box-sizing: border-box;
			border-bottom: 1rpx solid #cdcdcd;

			.left {
				.taskname {
					max-width: 550rpx;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
					font-size: 28rpx;
					color: #333;
				}

				.taskdesc {
					max-width: 510rpx;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
					font-size: 26rpx;
					color: #999;
					margin-top: 14rpx;
				}
			}

			.right {
				padding: 16rpx 34rpx;
				border-radius: 50rpx;
				background-color: #4d7df9;
				text-align: center;
				font-size: 28rpx;
				color: #fff;

				&.finish {
					background-color: #b8b8b8 !important;
				}
			}
		}
	}
</style>