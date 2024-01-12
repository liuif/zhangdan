<template>
	<view>
		<!-- 标题 -->
		<view class="title">
			<text class="title-text">预算明细</text>
		</view>

		<!-- 上方区域 -->
		<view class="top-area">
			<!-- 日历按钮 -->
			<yearsPicker :defaultValue="obtainYear" start="1950-01" :end="endYear" placeholder="请选择时间"
				@change="bindObtainYear"></yearsPicker>
		</view>

		<!-- 下方区域 -->
		<view class="bottom-area">
			<!-- 区域内容 -->
			<view class="t-task-item" v-for="book in books" :key="book">
				<view class="left">
					<view class="taskname">{{ book.ledgername }}</view>
					<view class="taskdesc">预算金额</view>
				</view>
				<view class="right" style="width: 50px;" hover-class="t-hc" @click="saveid(book.ledgerid)">
					{{ book.budget }}
				</view>
			</view>
			<view class="fund-list">
				<view v-for="budget in budgets" :key="budget" class="fund-item">
					<view class="fund-info">
						<text class="fund-type">{{ budget.typename }}</text>
						<text class="fund-balance">{{ budget.budget }}</text>
						<text class="fund-balance">{{ budget.currentbudget }}</text>
						<button @click="deleteBudget(budget)">删除</button>
					</view>
				</view>
			</view>

			<view class="add-fund">
				<input v-model="newExpenseType" type="text" placeholder="请输入消费类型" pattern="[0-9]+([.]{0,1}[0-9]+)?">
				<input class="add-fund-balance" v-model="newExpenseBudget" type="number" placeholder="请输入预算">
				<button class="add-btn" @click="addBudget">添加</button>
			</view>
		</view>
	</view>
</template>

<script>
	import moment from "moment";
	import yearsPicker from '@/components/years-picker/years-picker.vue'
	export default {
		components: {
			yearsPicker
		},
		data() {
			return {
				obtainYear: moment().format("Y-M月"), // 获取当前年月
				endYear: "2300-12",
				budgets: [],
				useremail: uni.getStorageSync('email'),
				newExpenseType: '',
				newExpenseBudget: '',
				//budgets:[],
			};
		},

		methods: {
			bindObtainYear(newYear) {
				this.obtainYear = newYear;
				this.getBudgetList();
			},
			// 从后台获取列表
			getBudgetList() {
				console.log(this.obtainYear);
				uni.request({
					url: '/api/getBudgetDetails',
					method: 'POST',
					data: {
						ledger_id: uni.getStorageSync('ledger_budget'),
						period: this.obtainYear,
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.budgets = res.data.data
						} else {
							uni.showToast({
								title: '获取预算明细失败，请稍后重试',
								icon: 'none',
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
			},
			// 添加
			addBudget() {
				if ((this.newExpenseType !== '') && (this.newExpenseBudget !== '')) {
					uni.showLoading({
						title: '添加中...',
						mask: true
					})
					const data = {
						ledger_id: uni.getStorageSync('ledger_budget'),
						email: uni.getStorageSync('email'),
						typename: this.newExpenseType,
						budget: parseFloat(this.newExpenseBudget).toFixed(2),
						period: this.obtainYear,
					}
					console.log(data);
					uni.request({
						url: '/api/updateBudgetDetails',
						method: 'POST',
						data,
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.showToast({
									title: '添加成功'
								})
								this.newExpenseType = ''
								this.newExpenseBudget = ''
								this.getBudgetList()
							} else {
								// uni.showToast({
								// 	title: '添加失败，请稍后重试',
								// 	icon: 'none'
								// })
							}
						},
						fail: () => {
							uni.showToast({
								title: '网络异常，请稍后重试',
								icon: 'none'
							})
						},
						complete: () => {
							uni.hideLoading()
						}
					})
				}
			},
			deleteBudget(budget) {
				uni.showModal({
					title: '提示',
					content: '确认删除该预算吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: '/api/removeBudget',
								method: 'POST',
								data: {
									type_id: budget.typeid,
									ledger_id: uni.getStorageSync('ledger_budget'),
									period: this.obtainYear,
								},
								success: (res) => {
									console.log(res);
									if (res.statusCode === 200) {
										uni.showToast({
											title: '删除成功'
										})
										this.getBudgetList()
									} else {
										// uni.showToast({
										// 	title: '删除失败，请稍后重试',
										// 	icon: 'none'
										// })
									}
								},
								fail: () => {
									uni.showToast({
										title: '网络异常，请稍后重试',
										icon: 'none'
									})
								},
								complete: () => {
									uni.hideLoading()
								}
							})
						}
					}
				})
			},
		},
		mounted() {
			this.getBudgetList()
		}

	};
</script>

<style>
	.title {
		text-align: center;
		padding: 20rpx;
		font-size: 24rpx;
		font-weight: bold;
		display: flex;
		justify-content: space-between;
	}

	.title-text {
		flex: 1;
	}

	.calendar-button {
		font-size: 24rpx;
		cursor: pointer;
	}

	.top-area,
	.bottom-area {
		padding: 20rpx;
		margin-top: 10rpx;
		background-color: #f0f0f0;
		/* 区域样式 */
	}

	.container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 20px;
	}

	.header {
		margin-bottom: 20px;
	}

	.title {
		font-size: 20px;
		font-weight: bold;
	}

	.fund-list {
		width: 100%;
	}

	.fund-item {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10px;
	}

	.fund-info {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.fund-type {
		font-weight: bold;
		margin-right: 10px;
	}

	.fund-balance {
		width: 120px;
	}

	.fund-count {
		padding: 5px;
		border: 1px solid #ccc;
		border-radius: 3px;
		font-size: 12px;
	}

	.add-fund {
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.add-fund input {
		margin-right: 10px;
	}

	.add-btn {
		padding: 5px 10px;
		background-color: #007aff;
		color: #fff;
		border: none;
		border-radius: 5px;
		font-size: 14px;
	}
</style>