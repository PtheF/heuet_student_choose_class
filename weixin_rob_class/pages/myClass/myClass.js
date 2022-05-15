// pages/myClass/myClass.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myClass: []
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

        let auth = wx.getStorageSync('authorization');

        wx.request({
            url: "http://localhost:8080/class/my",
            method: "GET",
            header: {
                authorization: auth,
            },

            success: (res) => {
                res = res.data;

                console.log(res);

                if(res.success){
                    this.setData({
                        myClass: res.data
                    })
                }else{

                }
            },

            fail: (err) => {
                wx.showModal({
                    title: "服务异常",
                    content: "稍后再试",
                    cancelColor: 'cancelColor',
                })
            }
        })
    },

    back(){
        wx.navigateBack();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})