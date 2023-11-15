import * as THREE from 'https://cdn.skypack.dev/three@0.132.2';
import { GLTFLoader } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/loaders/GLTFLoader';

const container = document.getElementById('model-container-2');
const containerRect = container.getBoundingClientRect();

const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, containerRect.width / containerRect.height, 0.1, 5000);
const renderer = new THREE.WebGLRenderer();
renderer.setSize(containerRect.width, containerRect.height);
container.appendChild(renderer.domElement);

let model;
let moveForward = false;
let moveBackward = false;
let moveLeft = false;
let moveRight = false;

let mouseDown = false;
let prevMouseX = 0;
let prevMouseY = 0;

const turnSpeedX = 0.002; // Adjust the horizontal turn speed
const turnSpeedY = 0.002; // Adjust the vertical turn speed
const maxVerticalRotation = Math.PI / 3; // Reduced threshold

// Set the camera rotation order to "YXZ"
camera.rotation.order = 'YXZ';

document.addEventListener('mousemove', (event) => {
    if (mouseDown) {
        const deltaX = event.clientX - prevMouseX;
        const deltaY = event.clientY - prevMouseY;

        // Vertical drag
        camera.rotation.x -= deltaY * turnSpeedY;

        // Limit vertical rotation to prevent camera flipping
        camera.rotation.x = Math.max(-maxVerticalRotation, Math.min(maxVerticalRotation, camera.rotation.x));

        // Horizontal drag
        camera.rotation.y -= deltaX * turnSpeedX;

        // Update previous mouse position
        prevMouseX = event.clientX;
        prevMouseY = event.clientY;
    }
});

document.addEventListener('mousedown', (event) => {
    if (event.button === 0) {
        mouseDown = true;
        prevMouseX = event.clientX;
        prevMouseY = event.clientY;
    }
});

document.addEventListener('mouseup', () => {
    mouseDown = false;
});

const onKeyDown = (event) => {
    switch (event.key) {
        case 'w':
            moveForward = true;
            break;
        case 's':
            moveBackward = true;
            break;
        case 'a':
            moveLeft = true;
            break;
        case 'd':
            moveRight = true;
            break;
    }
};

const onKeyUp = (event) => {
    switch (event.key) {
        case 'w':
            moveForward = false;
            break;
        case 's':
            moveBackward = false;
            break;
        case 'a':
            moveLeft = false;
            break;
        case 'd':
            moveRight = false;
            break;
    }
};

document.addEventListener('keydown', onKeyDown);
document.addEventListener('keyup', onKeyUp);

const loader = new GLTFLoader();
loader.load('3D Models/modified_lamborghini_urus.glb', (gltf) => {
    model = gltf.scene;
    scene.add(model);

    // Set the initial camera position
    camera.position.set(10, 12, 28);

    animate();
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

    // Move the camera based on the keyboard input
    const moveSpeed = 0.1;

    // Calculate the movement vectors in the camera's local coordinate system
    const frontVector = new THREE.Vector3(0, 0, -1).applyQuaternion(camera.quaternion);
    const rightVector = new THREE.Vector3(1, 0, 0).applyQuaternion(camera.quaternion);

    if (moveForward) {
        camera.position.addScaledVector(frontVector, moveSpeed);
    }
    if (moveBackward) {
        camera.position.addScaledVector(frontVector, -moveSpeed);
    }
    if (moveLeft) {
        camera.position.addScaledVector(rightVector, -moveSpeed);
    }
    if (moveRight) {
        camera.position.addScaledVector(rightVector, moveSpeed);
    }

    // Render the scene
    renderer.render(scene, camera);
}