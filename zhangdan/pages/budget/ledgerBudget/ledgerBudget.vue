<template>
	<view class="content">
		<view class="t-task">
			<view class="t-task-item" v-for="book in books" :key="book">
				<view class="left">
					<view class="taskname">{{ book.ledgername }}</view>
					<view class="taskdesc">预算金额</view>
				</view>
				<view class="right" style="width: 50px;" hover-class="t-hc" @click="saveid(book.ledgerid)">
					{{ book.budget }}
				</view>
				<view class="right" style="width: 50px;" hover-class="t-hc" @click="saveid(book.ledgerid)">
					{{ book.currentbudget }}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				books: [], // 账本列表
				budgets: [],
				useremail: uni.getStorageSync('email')
			};
		},
		methods: {
			getTotalBudget() {
				uni.request({
					url: '/api/getTotalBudget',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email'),
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.books = res.data.ledger;
						} else {
							uni.showToast({
								title: '获取预算明细失败，请稍后重试',
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
			saveid(id) {
				uni.setStorageSync('ledger_budget', id);
				console.log(id);
				uni.navigateTo({
					url: '/pages/budget/setBudget/setBudget'
				});
			},
		},
		mounted() {
			this.getTotalBudget()
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