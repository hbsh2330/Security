		const formData = new FormData();	//폼관련 정보 저장



		const uploadBox_el = document.querySelector('.upload-box');
		//dragenter / dragover /dragleave / drop

		uploadBox_el.addEventListener('dragenter',function(e){
			e.preventDefault();
			console.log("dragenter...");
		});
		uploadBox_el.addEventListener('dragover',function(e){
			e.preventDefault();
			uploadBox_el.style.opacity='0.5';
			console.log("dragover...");

		});
		uploadBox_el.addEventListener('dragleave',function(e){
			e.preventDefault();
			uploadBox_el.style.opacity='1';
			console.log("dragleave...");

		});

        //----------------------------------------------
        //
        //----------------------------------------------
		uploadBox_el.addEventListener('drop',function(e){
			e.preventDefault();
			console.log("drop...");
//			console.log(e);
//			console.log(e.dataTransfer);
			console.log(e.dataTransfer.files[0]);


//            const file = e.dataTransfer.files[0];

            //유효성 체크 filter , map
            const imgFiles= Array.from(e.dataTransfer.files).filter(f=> f.type.startsWith('image/'));
            if(imgFiles.length===0){
                alert("이미지 파일만 가능합니다.")
                return false;
            }
            //이미지의 개수 5개 제한
            //이미지 하나당 사이즈 제한..
            imgFiles.forEach(file=>{
                if(file.size>(1024*1024*5)){
                    alert("파일하나당 최대 사이즈는 5Mb이하여야 합니다..")
                    return false;
                }
            })


            for(var file of imgFiles ){
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload=function(e){
                    const preview = document.querySelector('#preview');
                    const imgEl =  document.createElement('img');
    //                console.log("reader.onload",e)
                    imgEl.setAttribute('src',e.target.result);
                    preview.appendChild(imgEl);
                }
                formData.append('files',file);
                console.log("formData",formData);
            }

		});


		const add_product_btn_el = document.querySelector('.add_product_btn');
		add_product_btn_el.addEventListener('click',function(){
            const seller = document.imageform.seller.value;
            const productname = document.imageform.productname.value;
            const category = document.imageform.category.value;
            const brandname = document.imageform.brandname.value;
            const itemdetals = document.imageform.itemdetals.value;
            const amount = document.imageform.amount.value;
            const size = document.imageform.size.value;

            const price = document.imageform.price.value;

            formData.append('seller',seller);
            formData.append('productname',productname);
            formData.append('category',category);
            formData.append('brandname',brandname);
            formData.append('itemdetals',itemdetals);
            formData.append('amount',amount);
            formData.append('size',size);
            formData.append('price',price);

            axios.post('/imageboard/add',formData,{ headers: {'Content-Type' :'multipart/form-data' } } ) //파일일 경우 'multipart/form-data'추가
            .then(res=>{
                console.log(res);
                alert("물품 등록을 완료했습니다.")
                location.href="/imageboard/list";
            })
            .catch(err=>{console.log(err);})


		})