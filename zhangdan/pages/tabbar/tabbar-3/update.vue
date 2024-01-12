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
		<button style="margin-top: 10px;" @click="submitData">修改 </button>
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
						options: [{
							label: "餐饮美食",
							checked: false
						}]
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
				note: "",
				id: 1,
			};
		},
		mounted() {
			this.getTypeList();
		},
		onLoad(options) {
			if (options.bill) {
				const bill = JSON.parse(options.bill);
				this.id = bill.id;
				this.selectedAmount = bill.money;
				this.note = bill.note;
				this.selectedDate.time = bill.date;
				this.selectedOptions.type = bill.typeName;
				for (const group of this.optionGroups) {
					if (group.name === "收入/支出") {
						// 遍历 options
						for (const option of group.options) {
							if (option.label === bill.incomeOrExpense) {
								option.checked = true; // 找到匹配的选项，设置为选中状态
								this.selectedOptions.inout = option.label;
							}
						}
					}
				}
			}
		},
		methods: {
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
								if (item === this.selectedOptions.type) {
									return {
										label: item,
										checked: true,
									}
								} else {
									return {
										label: item,
										checked: false
									};
								}
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
				if (groupIndex == 0) {
					this.selectedOptions.type = selectedOption.label;
				} else {
					this.selectedOptions.inout = selectedOption.label;
				}
			},
			picked(pickedDate) {
				if (typeof pickedDate === 'object' && pickedDate !== null) {
					this.selectedDate = pickedDate.time
				}
			},
			submitData() {
				const data = {
					typename: this.selectedOptions.type,
					inorout: this.selectedOptions.inout,
					date: this.selectedDate.time,
					money: parseFloat(this.selectedAmount).toFixed(2),
					email: uni.getStorageSync('email'),
					ledger_id: uni.getStorageSync('ledger'),
					id: this.id,
					note: this.note,
				};
				console.log(data);
				uni.request({
					url: '/api/UpdateBill',
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
							title: '修改成功',
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
			},
		},
	};
</script>
<style>
	.btn {
		margin: 10px;
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 5px;
		cursor: pointer;
	}
</style>