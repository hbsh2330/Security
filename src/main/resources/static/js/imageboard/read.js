const mainImgEl = document.querySelector('.upload-box > img')

const previewEls = document.querySelectorAll('#preview > img');
previewEls.forEach(item=>{
    item.addEventListener('click', function (){
        mainImgEl.src = item.src;
    })
})


// const mainImgEl = document.querySelector('.upload-box img');
//
// const previewEls = document.querySelectorAll('#preview img');
// previewEls.forEach(item=>{
//     item.addEventListener('click',function(){
//         mainImgEl.src = item.src;
//     })
//
// })