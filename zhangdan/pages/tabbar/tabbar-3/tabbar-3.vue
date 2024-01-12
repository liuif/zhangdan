<template>
	<view>
		<view v-for="(optionGroup, index) in optionGroups" :key="index">
			<h3>{{ optionGroup.name }}</h3>
			<radio-group @selected="handleSelectedOption">
				<view style="width: 100%; display: flex; flex-wrap: wrap;">
					<view style="width: 30%; display: flex; flex-wrap: wrap; flex: 1; margin-right: 10px;">
						<view style="margin: 5px 5px;" v-for="(option, optionIndex) in optionGroup.options"
							:key="optionIndex">
							<u-tag :text="option.label" :plain="!option.checked" type="primary" :name="optionIndex"
								@click="radioClick(index, optionIndex)"></u-tag>
						</view>
					</view>
				</view>
			</radio-group>
		</view>
		<view>
			<h3><label for="date">选择日期：</label></h3>
			<date-picker v-model="selectedDate" :date="selectedDate"></date-picker>
		</view>
		<view>
			<h3><label for="amount">输入金额：</label></h3>
			<input
				style="margin-top: 5px;width: 95%;margin-left: 2.5%; height: 30px; box-shadow: 0 0 3px rgba(0, 0, 0, 0.8); border-radius: 5%;"
				type="number" step="0.01" v-model="selectedAmount" pattern="[0-9]+([.]{0,1}[0-9]+)?" />
		</view>
		<view>
			<h3><label for="amount">备注：</label></h3>
			<input
				style="margin-top: 5px; margin-bottom: 10px; width: 95%;margin-left: 2.5%; height: 30px; box-shadow: 0 0 3px rgba(0, 0, 0, 0.8); border-radius: 5%;"
				type="text" v-model="note" />
		</view>
		<button style="margin-top: 10px;" @click="submitData">添加 </button>
		<view class="camera-icon">
			<image class="camera-icon-inner" src="/static/img/camera.png" @click="chooseImage"></image>
		</view>
	</view>
</template>

<script>
	import DatePicker from 'vue-datepicker';
	export default {
		components: {
			DatePicker // 注册插件为组件
		},
		data() {
			return {
				optionGroups: [{
						name: "消费类型",
						options: []
					},
					{
						name: "收入/支出",
						options: [{
								label: "收入",
								checked: false
							},
							{
								label: "支出",
								checked: false
							}
						]
					},
				],
				selectedOptions: {
					type: "",
					inout: ""
				},
				selectedDate: {},
				selectedAmount: "",
				note: ""
			};
		},
		onLoad(options) {
			if (options.bill) {
				const bill = JSON.parse(options.bill);
				this.selectedAmount = bill.money;
				this.note = bill.note;
				this.selectedDate.time = bill.date;
			}
		},
		methods: {
			convert(path) {
				return new Promise((resolve, reject) => {
					// #ifdef APP-PLUS
					plus.io.resolveLocalFileSystemURL(path, (entry) => {
						entry.file((file) => {
							let fileReader = new plus.io.FileReader();
							fileReader.readAsDataURL(file);
							fileReader.onloadend = (evt) => {
								let base64 = evt.target.result.split(",")[1];
								resolve(base64);
							};
						});
					});
					// #endif
					// #ifdef H5
					uni.request({
						url: path,
						responseType: 'arraybuffer',
						success: (res) => {
							resolve(uni.arrayBufferToBase64(res.data));
						},
					});
					// #endif
					// #ifdef MP-WEIXIN
					uni.getFileSystemManager().readFile({
						filePath: path,
						encoding: 'base64',
						success: (res) => {
							resolve(res.data);
						},
					});
					// #endif
				});
			},
			chooseImage() {
				uni.chooseImage({
					count: 1,
					success: (res) => {
						uni.showLoading({
							title: '正在识别中...',
						});

						this.convert(res.tempFilePaths[0])
							.then((base64) => {
								// 创建 FormData 对象
								let formData = new FormData();
								formData.append('file', base64);
								// 将 FormData 转换为 JSON 字符串
								let jsonData = {};
								for (let [key, value] of formData.entries()) {
									jsonData[key] = value;
								}
								let jsonString = JSON.stringify(jsonData);
								// 将图像数据发送到Flask服务器
								uni.request({
									url: 'http://127.0.0.1:5000/process_image',
									method: 'POST',
									header: {
										'Content-Type': 'application/json',
									},
									data: jsonString, // 直接发送 JSON 字符串
									success: (res) => {
										uni.hideLoading();
										console.log('Processed Result:', res.data);
										this.note = JSON.stringify(res.data.remarks);
										const str = res.data.paid_amount;
										const regex = /\d+/g;
										const numbers = str.match(regex) || [];
										const formattedNumbers = numbers.join('.');
										if (formattedNumbers) {
											this.selectedAmount = formattedNumbers;
											console.log(formattedNumbers); // 输出 "123.45"
										} else {
											console.log("字符串中没有数字");
										}
									},
									fail: (err) => {
										uni.hideLoading();
										console.error(err);
									},
								});
							})
							.catch((err) => {
								uni.hideLoading();
								console.error(err);
							});
					},
					fail(err) {
						uni.hideLoading();
						console.log(err);
					},
				});
			},

			// 从后台获取消费类型列表
			getTypeList() {
				uni.request({
					url: '/api/getExpenseType',
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
									label: item,
									checked: false
								};
							});
							this.optionGroups[0].options = transformedData;
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
			radioClick(groupIndex, optionIndex) {
				const selectedOption = this.optionGroups[groupIndex].options[optionIndex];
				if (selectedOption.checked) {
					// 如果当前选项已经被选中，则取消选中状态
					selectedOption.checked = false;
				} else {
					// 否则，设为选中状态
					selectedOption.checked = true;
					// 取消其他选项的选中状态
					this.optionGroups[groupIndex].options.forEach((option, index) => {
						if (index !== optionIndex) {
							option.checked = false;
						}
					});
				}
				console.log(selectedOption.label);
				console.log(groupIndex);
				if (groupIndex == 0) {
					uni.setStorageSync('type', selectedOption.label);
				} else {
					uni.setStorageSync('inout', selectedOption.label);
				}
			},
			picked(pickedDate) {
				if (typeof pickedDate === 'object' && pickedDate !== null) {
					this.selectedDate = pickedDate.time
				}
			},
			submitData() {
				const data = {
					typename: uni.getStorageSync('type'),
					inorout: uni.getStorageSync('inout'),
					note: this.note,
					date: this.selectedDate.time,
					money: parseFloat(this.selectedAmount).toFixed(2),
					email: uni.getStorageSync('email'),
					ledger_id: uni.getStorageSync('ledger'),
				};
				console.log(data);
				uni.request({
					url: '/api/addbill',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data
				}).then((res) => {
					const result = res[1].data;
					if (!result) {
						console.error('登录接口返回数据为空');
						return;
					}
					if (result.code === 0) {
						// 登录成功
						uni.showToast({
							title: '添加成功',
							icon: 'success'
						});
						this.notifyBudgetExceedance();

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
				// 发送数据给后端处理
			},
			notifyBudgetExceedance() {
				uni.request({
					url: '/api/notifyBudgetExceedance',
					method: 'POST',
					data: {
						ledger_id: uni.getStorageSync('ledger'),
					},
					success: (res) => {
						console.log(res);
						// 处理成功响应，可以根据后台返回的数据进行处理
						if (res.statusCode === 200 && res.data.code === 1) {
							console.log("超支！！！！！！！！");
							uni.showModal({
								title: '提示',
								content: '该账本预算即将超支',
								success: (res) => {}
							})
							this.$emit('showBudgetExceedancePopup', true);
						}
					},
					fail: () => {
						// 处理失败响应
						console.log('调用 notifyBudgetExceedance 失败');
					}
				});
			}
		},
		mounted() {
			this.getTypeList()
		}
	};
</script>
<style>
	.camera-icon {
		color: white;
		width: 50px;
		height: 50px;
		background-color: whitesmoke;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		position: fixed;
		bottom: 80px;
		right: 20px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

	.camera-icon-inner {
		width: 40px;
		height: 40px;
	}
</style>