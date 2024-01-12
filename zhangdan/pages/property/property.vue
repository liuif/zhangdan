<template>
	<view class="container">
		<view class="header">
			<text class="title">资金管理</text>
		</view>

		<view class="fund-list">
			<view v-for="(fund, index) in funds" :key="index" class="fund-item">
				<view class="fund-info">
					<text class="fund-type">{{ fund.type }}</text>
					<input class="fund-balance" v-model="fund.balance" type="number" placeholder="请输入余额"><button
						@click="deleteProperty(fund.type)">删除</button>
				</view>

			</view>
		</view>

		<view class="add-fund">
			<input v-model="newFundType" type="text" placeholder="请输入类型">
			<input class="add-fund-balance" v-model="newFundBalance" type="number" placeholder="请输入余额">
			<button class="add-btn" @click="addFund">添加</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				funds: [{
						type: '现金',
						balance: 1000
					},
					{
						type: '支付宝',
						balance: 500
					},
					{
						type: '银行卡',
						balance: 2000
					}
				],
				useremail: uni.getStorageSync('email'),
				newFundType: '',
				newFundBalance: '',
				//funds:[],
			};
		},

		methods: {
			// 从后台获取列表
			getPropertyList() {
				uni.request({
					url: '/api/getProperty',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email')
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							const backendData = res.data.data;
							console.log(backendData);
							const transformedData = backendData.map(item => {
								return {
									balance: item.property_balance,
									type: item.property_name
								};
							});
							console.log(transformedData);
							this.funds = transformedData;
						} else {
							uni.showToast({
								title: '获取资金列表失败，请稍后重试',
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
			},
			// 添加
			addFund() {
				if ((this.newFundType !== '') && (this.newFundBalance !== '')) {
					const data = {
						email: uni.getStorageSync('email'),
						property_name: this.newFundType,
						balance: parseFloat(this.newFundBalance).toFixed(2),
					};
					console.log(data);
					uni.request({
						url: '/api/addProperty',
						method: 'POST',
						data,
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.showToast({
									title: '添加成功'
								})
								this.newnewFundType = ''
								this.newFundBalance = ''
								this.getPropertyList()
							} else {
								uni.showToast({
									title: '添加失败，请稍后重试',
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
			},
			//删除
			deleteProperty(Id) {
				console.log(Id);
				uni.showModal({
					title: '提示',
					content: '确认删除该资金类型吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showLoading({
								title: '删除中...',
								mask: true
							})
							uni.request({
								url: '/api//removeProperty',
								method: 'POST',
								data: {
									email: uni.getStorageSync('email'),
									type: Id
								},
								success: (res) => {
									console.log(res);
									if (res.statusCode === 200 && res.data.code === true) {
										uni.showToast({
											title: '删除成功'
										})
										this.getPropertyList()
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
			}
		},
		mounted() {
			this.getPropertyList()
		}
	};
</script>

<style>
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