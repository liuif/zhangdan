<template>
	<view class="content">
		<view class="t-task">

			<view class="t-task-item" v-for="book in books" :key="book">
				<view
					style="margin-top: 5px; background-color: aliceblue; width: 100%;height: 50px; box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);display: flex; flex-wrap: wrap;;">
					<button style="width: 60%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="changeledger(book.ledger_id)">{{ book.ledger_name }}</button>
					<button style="width: 22%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="getinvitation(book.ledger_id)">邀请码</button>
					<button style="width: 18%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="deleteBook(book)">删除</button>
				</view>
			</view>

			<view style="margin-top: 20px;" v-for="share in shares" :key="share">
				<view
					style="margin-top: 5px; background-color: bisque; width: 100%;height: 50px; box-shadow: 0 0 3px rgba(0, 0, 0, 0.8);display: flex; flex-wrap: wrap;">
					<button style="width: 60%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="changeledger(share.ledger_id)">{{ share.ledger_name }}</button>
					<button style="width: 22%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="getinvitation(share.ledger_id)">邀请码</button>
					<button style="width: 18%;height: 50px;margin-bottom: 0%;font-size: 15px;margin-right: 0%;"
						@click="deleteBook(share)">删除</button>
				</view>
			</view>
		</view>
		<view style="margin-top: 20px;">
			<view class="manage-book">
				<input class="book-name-input" placeholder="输入账本名称" v-model="newBookName" />
				<button class="add-book" @click="addNewBook">新增账本</button>
			</view>

			<view class="invitation">
				<input class="invitation-input" placeholder="输入邀请码" v-model="invitationCode" />
				<button class="join-book" @click="joinBook">加入账本</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				useremail: uni.getStorageSync('email'),
				books: [], // 账本列表
				newBookName: '', // 新增账本的名称
				invitationCode: '', // 输入的邀请码
			};
		},
		methods: {
			getBookList() {
				uni.request({
					url: '/api/getledger',
					method: 'POST',
					data: {
						email: uni.getStorageSync('email')
					},
					success: (res) => {
						console.log(res);
						if (res.statusCode === 200) {
							this.books = res.data.ledger;
							this.shares = res.data.shared;
							if (uni.getStorageSync('ledger') === -1) {
								uni.setStorageSync('ledger', result.data.ledger_id);
							}
						} else {
							uni.showToast({
								title: '获取账本列表失败，请稍后重试',
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
			addNewBook() {
				if (this.newBookName !== '') {
					uni.request({
						url: '/api/addledger',
						method: 'POST',
						data: {
							ledgerName: this.newBookName,
							email: uni.getStorageSync('email')
						},
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.showToast({
									title: '添加成功'
								})
								this.newBookName = '',
									this.invitationCode = ''
								this.getBookList()
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
			deleteBook(book) {
				console.log(book);
				uni.showModal({
					title: '提示',
					content: '确认删除该账本吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: '/api/removeLedger',
								method: 'POST',
								data: {
									ledgerId: book.ledger_id,
									ledgerName: book.ledger_name,
									email:uni.getStorageSync("email"),
								},
								success: (res) => {
									console.log(res);
									if (res.statusCode === 200 && res.data.code === 0) {
										uni.showToast({
											title: '删除成功'
										})
										this.getBookList()
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
			changeledger(book) {
				console.log(book);
				uni.showModal({
					title: '提示',
					content: '确认切换至该账本吗？',
					success: (res) => {
						uni.setStorageSync('ledger', book);
						uni.reLaunch({
						  url: '/pages/tabbar/tabbar-1/tabbar-1',
						  success: function() {
						    let page = getCurrentPages().pop();
						    if (page && page.onLoad) {
						      page.onLoad();
						    }
						  }
						});
					}
				})
			},
			getinvitation(book) {
				console.log(book);
				uni.showModal({
					title: '提示',
					content: '确认分享该账本吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: '/api/getinvitation',
								method: 'POST',
								data: {
									ledgerId: book,
								},
								success: (res) => {
									console.log(res);
									if (res.statusCode === 200 && res.data.code === 0) {
										uni.showModal({
											title: '邀请码为',
											content: res.data.invitation,
										})
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
			joinBook() {
				if (this.invitationCode !== '') {
					uni.showLoading({
						title: '添加中...',
						mask: true
					})
					uni.request({
						url: '/api/joinLedger',
						method: 'POST',
						data: {
							email: uni.getStorageSync('email'),
							invitation: this.invitationCode
						},
						success: (res) => {
							console.log(res);
							if (res.statusCode === 200) {
								uni.showToast({
									title: '添加成功'
								})
								this.getBookList()
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
			}
		},
		mounted() {
			this.getBookList()
		}
	};
</script>

<style>
	.container {
		padding: 20px;
	}

	.book {
		margin-left: 5%;
		width: 90%;
		height: 200px;
		background-color: #f0f0f0;
		border-radius: 8px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-bottom: 20px;
	}

	.book-info {
		text-align: center;
	}

	.book-name {
		font-size: 16px;
		margin-bottom: 5px;
	}

	.invitation-code {
		font-size: 14px;
		color: #666;
	}

	.manage-book {
		display: flex;
		align-items: center;
		margin-bottom: 20px;
	}

	.add-book {
		margin-left: 10px;
		padding: 10px;
	}

	.book-name-input {
		flex: 1;
		padding: 10px;
	}

	.invitation {
		display: flex;
		align-items: center;
	}

	.invitation-input {
		flex: 1;
		padding: 10px;
	}

	.join-book {
		margin-left: 10px;
		padding: 10px;
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