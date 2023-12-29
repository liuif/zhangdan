<template>
	<view class="container">
		<view class="type-list">
			<!-- <block v-if="types.length > 0">
				<view class="type-item" v-for="(item, index) in types" :key="index">{{item}}</view>
			</block> -->
			<view v-for="type in types" :key="type">
				<view><text>{{ type }}</text><button @click="deleteType(type)">删除</button></view>
			</view>
			<!-- <view v-else class="empty-tip">暂无消费类型</view> -->
		</view>
		<view class="add-type-box">
			<input class="add-type-input" placeholder="请输入消费类型" v-model="newType">
			<view class="add-type-btn" @tap="addType">添加</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				newType: '', // 新添加的消费类型
				types: [], // 消费类型列表
			}
		},
		methods: {
			// 从后台获取消费类型列表
			getTypeList() {
				uni.request({
					url: '/api/getExpenseType',
					method: 'POST',
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.types = res.data.data
						} else {
							uni.showToast({
								title: '获取消费类型列表失败，请稍后重试',
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
			// 添加消费类型
			addType() {
				if (this.newType !== '') {
					uni.showLoading({
						title: '添加中...',
						mask: true
					})
					uni.request({
						url: '/api/addExpenseType',
						method: 'POST',
						data: {
							typename: this.newType
						},
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.showToast({
									title: '添加成功'
								})
								this.newType = ''
								this.getTypeList()
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
			//删除消费类型
			deleteType(typeId) {
				console.log(typeId);
				uni.showModal({
					title: '提示',
					content: '确认删除该消费类型吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showLoading({
								title: '删除中...',
								mask: true
							})
							uni.request({
								url: '/api/removeExpenseType',
								method: 'POST',
								data: {
									typename: typeId
								},
								success: (res) => {
									console.log(res);
									if (res.statusCode === 200 && res.data.code === true) {
										uni.showToast({
											title: '删除成功'
										})
										this.getTypeList()
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
			this.getTypeList()
		}
	}
</script>

<style>
	.container {
		padding: 20rpx;
		font-size: 32rpx;
		background-color: #ffffff;
		height: 100%;
	}

	.type-list {
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #eaeaea;
	}

	.type-item {
		padding: 10rpx 20rpx;
	}

	.empty-tip {
		padding: 10rpx 0;
		text-align: center;
		color: #aaaaaa;
	}

	.add-type-box {
		display: flex;
	}

	.add-type-input {
		flex: 1;
		margin-right: 20rpx;
		padding: 10rpx 20rpx;
		border: none;
		border-radius: 20rpx;
		background-color: #eeeeee;
	}

	.add-type-btn {
		padding: 10rpx 20rpx;
		border: none;
		border-radius: 20rpx;
		color: #ffffff;
		background-color: #007aff;
	}
</style>