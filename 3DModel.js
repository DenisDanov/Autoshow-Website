import * as THREE from 'https://cdn.skypack.dev/three@0.132.2';
import { GLTFLoader } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/controls/OrbitControls';

const container = document.getElementById('model-container');
const containerRect = container.getBoundingClientRect();

const aspectRatio = containerRect.width / containerRect.height;
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, aspectRatio, 0.1, 5000);
const renderer = new THREE.WebGLRenderer();
const controls = new OrbitControls(camera, renderer.domElement);

renderer.setSize(containerRect.width, containerRect.height);
container.appendChild(renderer.domElement);

let model;
let autoRotate = true;

// Create a pivot object
const pivot = new THREE.Group();
scene.add(pivot);

function setCameraPosition() {
    if (model) {
        // Calculate bounding box of the model
        const boundingBox = new THREE.Box3().setFromObject(model);
        const center = new THREE.Vector3();
        boundingBox.getCenter(center);

        // distance from the center to the camera position
        const boundingBoxSize = boundingBox.getSize(new THREE.Vector3());
        const cameraDistance = Math.max(boundingBoxSize.x, boundingBoxSize.y, boundingBoxSize.z) /
            Math.tan(THREE.MathUtils.degToRad(camera.fov / 2));

        // Set camera position
        camera.position.copy(center);
        if (containerRect.width > 430) {
            camera.position.z += cameraDistance - 30;
        } else {
            camera.position.z += cameraDistance - 20;
        }
       

        // Set controls target
        controls.target.copy(center);
    }
}

const loader = new GLTFLoader();
loader.load('3D Models/modified_lamborghini_urus.glb', (gltf) => {
    model = gltf.scene;

    // Center the geometry based on its bounding box
    const boundingBox = new THREE.Box3().setFromObject(model);
    boundingBox.getCenter(model.position);
    model.position.multiplyScalar(-1);

    // Add the model to the pivot
    pivot.add(model);

    setCameraPosition(); // Set the camera position after loading the model

}, undefined, (error) => {
    console.error('Error loading GLB model:', error);
});

const directionalLight = new THREE.DirectionalLight(0xffffff, 2.5);
directionalLight.position.set(5, 5, 5).normalize();
scene.add(directionalLight);

const ambientLight = new THREE.AmbientLight(0xffffff, 1); // Adjust intensity as needed
scene.add(ambientLight);

function animate() {
    requestAnimationFrame(animate);

    if (model && autoRotate) {
        // Rotate the pivot around the Y-axis
        pivot.rotation.y += 0.005;
    }

    controls.update();
    renderer.render(scene, camera);
}

animate();

window.addEventListener('resize', () => {
    const containerRect = container.getBoundingClientRect();
    camera.aspect = containerRect.width / containerRect.height;
    camera.updateProjectionMatrix();
    renderer.setSize(containerRect.width, containerRect.height);
});

// Add event listeners to stop automatic rotation when the user interacts
controls.addEventListener('start', () => {
    autoRotate = false;
});
