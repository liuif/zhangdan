<template>
	<view class="content">
		<view style="align-items: center; margin-bottom: 10px; font-size: 20px;">{{titleText}}</view>
		<view class="calendar">
			<as-calendar ref="as" :markDays="markDays" :headerBar="true" @onDayClick="onDayClick">
				<view slot="header" @click="navigateTo('/pages/tabbar/tabbar-1/myWork')">
					<view class="asTop align-items">
						<text> 日历 </text>
					</view>
				</view>
			</as-calendar>
		</view>
		<view class="content">
			<view class="t-task">
				<view class="t-task-item" v-for="bill in bills" :key="bill">
					<view
						style="margin-top: 10px; background-color: aliceblue; width: 350px; height: 40px; box-shadow: 0 0 3px rgba(0, 0, 0, 0.8); display: flex; flex-wrap: wrap; justify-content: space-between; align-items: center;">
						<button style="width: 250px; height: 40px; margin-bottom: 0%; font-size: 15px; margin-left: 0%;"
							@click="updatebill(bill)">
							<text style="margin-left: -5%; margin-top: -5px;">{{ bill.name }}</text>
							<text style="margin-left: 5%; margin-top: -5px;">{{ bill.incomeOrExpense }}</text>
							<text style="margin-left: 5%; margin-top: -5px;">{{ bill.typeName }}</text>
							<text style="margin-left: 5%; margin-top: -5px;">{{ bill.money }}</text>
						</button>
						<button
							style="width: 100px; height: 40px; margin-bottom: 0%; font-size: 15px; margin-right: 0%;background-color:#4d7df9 ;"
							@click="deletebill(bill.id)">删除账目</button>
					</view>
				</view>
			</view>
		</view>
		<router-link class="bill" to="/pages/tabbar/tabbar-3/tabbar-3">
			<text class="add">+</text>
		</router-link>
	</view>
</template>

<script>
	import asCalendar from '@/components/as-calendar/as-calendar.vue'

	export default {
		components: {
			asCalendar
		},
		computed: {
			navigationBarTitleText() {
				return this.titleText;
			}
		},
		data() {
			return {
				// 日历数据
				curDate: new Date().toISOString().slice(0, 10),
				markDays: ['2022-09-06'],
				bills: [],
				titleText: '',
			}
		},
		data1() {
			return {
				"email": "",
				"data": "",
			}
		},
		onReady() {
			// 初始化日历
			let today = this.$refs.as.getToday().date
			this.curDate = today
			// this.markDays.push(today)
			console.log(today, 'today')
		},
		methods: {
			getBookList() {
				if (uni.getStorageSync('ledger') === -1) {
					uni.request({
						url: '/api/getledger',
						method: 'POST',
						data: {
							email: uni.getStorageSync('email')
						},
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.setStorageSync('ledger', res.data.ledger_id);
							} else {
								uni.showToast({
									title: '获取账本失败，请稍后重试',
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
				return;
			},
			getBookName() {
				uni.request({
					url: '/api/getledgertname',
					method: 'POST',
					data: {
						ledger_id: uni.getStorageSync('ledger')
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.titleText = res.data.ledgername;
							console.log(res.data.ledgername);
						} else {
						
						}
					},
					fail: () => {
						uni.showToast({
							title: '网络异常，请稍后重试',
							icon: 'none'
						})
					}
				})
				return;
			},
			getbills(Data) {
				const data = {
					ledger_id: uni.getStorageSync('ledger'),
					date: Data
				};
				console.log(data);
				uni.request({
					url: '/api/getbillsbydata',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data,
				}).then((res) => {
					const result = res[1].data;
					console.log(res[1]);
					if (!result) {
						console.error('登录接口返回数据为空');
						console.log(res[1].data);
						console.log(res);
						console.log(result);
						return;
					}
					if (result.code === 0) {
						this.checkIfShared(result.data);
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
			},
			deletebill(Id) {
				console.log(Id);
				uni.showModal({
					title: '提示',
					content: '确认删除该账目吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: '/api/removeOneBill',
								method: 'POST',
								data: {
									id: Id
								},
								success: (res) => {
									console.log(res.data.code);
									if (res.statusCode === 200 && res.data.code === 1) {
										uni.showToast({
											title: '删除成功'
										})
										this.getbills(this.curDate);
									} else {
										uni.showToast({
											title: '删除失败，请稍后重试',
											icon: 'none'
										})
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
			checkIfShared(data) {
				uni.request({
					url: '/api/checkIfShared',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email'),
						ledger_id: uni.getStorageSync('ledger'),
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							if (res.data.code === 1) {
								this.bills = data;
							} else {
								const newData = data.map(item => {
									return {
										...item,
										name: "" // 修改 name 属性
									};
								});
								this.bills = newData;
							}
						} else {
							uni.showToast({
								title: '获取账本失败，请稍后重试',
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
				return 0;
			},
			updatebill(bill) {
				console.log(bill);
				uni.navigateTo({
					url: "/pages/tabbar/tabbar-3/update?bill=" + JSON.stringify(bill),
				});
			},
			// 日历点击事件
			onDayClick(data) {
				this.curDate = data.date
				console.log(`选中日期：${this.curDate}`)
				this.getbills(this.curDate);
			}
		},
		mounted() {
			this.getBookList();
			this.getBookName();
			this.getbills(this.curDate);
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 30rpx;
	}

	.asTop {
		display: flex;
		justify-content: space-between;
		align-items: center;
		text-align: start;
		justify-content: space-between;
		height: 80rpx;
		background: linear-gradient(360deg, #ffffff 0%, #e9f8ff 100%);
		padding: 0 30rpx;
		font-size: 30rpx;
		color: #111111;
	}

	.bill {
		width: 350px;
		height: 40px;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 20px;
		border-radius: 10px;
		background-color: #4d7df9;
		box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);
		position: relative;
	}

	.add {
		position: absolute;
		align-items: center;
		font-size: 30px;
	}

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
		}
	}
</style>