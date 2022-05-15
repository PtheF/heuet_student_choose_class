// pages/selectClass/selectClass.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
      elc: null,
      pes: null,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

      let auth = wx.getStorageSync('authorization');
      console.log(auth);
      
      wx.request({
        url: "http://localhost:8080/class/selectClass",
        method: "GET",
        header: {
          authorization: auth
        },
        success: (res) => {
          res = res.data;
          if(res.success){
            let elc = res.data.eles;
            let pes = res.data.pes;

            this.setData({elc: elc, pes: pes});
          }else{
            wx.showModal({
              title: "请求错误",
              content: "不知道为啥没正确返回",
              cancelColor: 'cancelColor',
            })
          }
        },
        fail: (err) => {
          wx.showModal({
            title: "服务异常",
            content: "服务异常，请稍后再试",
            cancelColor: 'cancelColor',
          })
        }
      })
    },

    robClass(e){

      let requestBody = {
        id: e.currentTarget.dataset.id,
        typ: e.currentTarget.dataset.typ
      };

      let auth = wx.getStorageSync('authorization')

      wx.request({
        url: 'http://localhost:8080/robClass/rob',
        method: "POST",
        data: requestBody,
        contentType: "application/json",
        header: {
          authorization: auth
        },

        success: (res) => {
          res = res.data;
          wx.showModal({
            title: res.data,
            cancelColor: 'cancelColor',
          })
        },

        fail: (err) => {

        }
      })
    },

    myClass(){
        wx.navigateTo({
          url: '../myClass/myClass'
        })
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