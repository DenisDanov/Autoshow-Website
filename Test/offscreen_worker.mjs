// offscreen_worker.js
const { FBXLoader } = require('three/addons/loaders/FBXLoader.js');

let canvas;
let ctx;

self.onmessage = function (ev) {
    if (ev.data.msg === 'offscreen') {
        canvas = ev.data.canvas;
        ctx = canvas.getContext('2d');
        loadModelAndTexture();
    }
};

function loadModelAndTexture() {
    const modelURL = 'Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX';
    const textureURL = 'Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017_display.png';

    const loader = new FBXLoader();

    // Load the model progressively using onProgress callback
    loader.load(
        modelURL,
        (fbx) => {
            self.postMessage({ msg: 'loadModel', model: fbx });
        },
        (xhr) => {
            if (xhr.lengthComputable) {
                const percentComplete = (xhr.loaded / xhr.total) * 100;
                self.postMessage({ msg: 'loadingProgress', progress: percentComplete });
            }
        },
        (error) => {
            console.error('Error loading FBX model:', error);
        }
    );

    // Load the texture asynchronously using THREE.ImageLoader
    const imageLoader = new THREE.ImageLoader();
    imageLoader.load(
        textureURL,
        (texture) => {
            self.postMessage({ msg: 'loadTexture', texture });
        },
        undefined,
        (error) => {
            console.error('Error loading texture:', error);
        }
    );
}
