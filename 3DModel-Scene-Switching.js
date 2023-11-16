import * as THREE from 'https://cdn.skypack.dev/three@0.132.2';
import { GLTFLoader } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/controls/OrbitControls';
let scene; // Define scene globally
let renderer; // Declare renderer globally
let model;

function initThirdPersonScript() {

    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();

    const aspectRatio = containerRect.width / containerRect.height;
    const camera = new THREE.PerspectiveCamera(75, aspectRatio, 0.1, 5000);
    renderer = new THREE.WebGLRenderer();
    const controls = new OrbitControls(camera, renderer.domElement);

    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

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
}

function disposeThirdPersonScript() {
    if (model) {
        // Dispose logic specific to your 3D library (e.g., Three.js)
        // For Three.js, you might use something like:
        model.traverse(child => {
            if (child.isMesh) {
                child.geometry.dispose();
                child.material.dispose();
            }
        });
        scene.remove(model);
        model = null; // Clear the global variable
    }
}

// First Person Script
function initFirstPersonScript() {

    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();

    const camera = new THREE.PerspectiveCamera(75, containerRect.width / containerRect.height, 0.1, 5000);
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

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
}

function disposeFirstPersonScript() {
    if (model) {
        // Dispose logic specific to your 3D library (e.g., Three.js)
        // For Three.js, you might use something like:
        model.traverse(child => {
            if (child.isMesh) {
                child.geometry.dispose();
                child.material.dispose();
            }
        });
        scene.remove(model);
        model = null; // Clear the global variable
    }
}

// Variable to keep track of the currently active script
let activeScript = null;

// Function to load and initialize a 3D model script into the specified container
function loadModelScript(initFunction, disposeFunction, scriptSrc, containerId) {
    // Dispose of the currently active script if any
    if (activeScript) {
        disposeFunction();
    }

    // Call the initialization function for the new script
    initFunction();

    // Update the 3D model container
    const container = document.getElementById(containerId);

    if (container) {
        container.innerHTML = ''; // Clear the container

        // Assuming renderer is the correct object to append
        container.appendChild(renderer.domElement);

        // Update the activeScript to the current scene or renderer
        activeScript = renderer; // Update this line based on your use case
    } else {
        console.error(`Container with ID ${containerId} not found.`);
    }
}

// Event listener for the First Person button
document.getElementById('firstPersonBtn').addEventListener('click', function () {
    // Load the first person script if it's not already active
    if (activeScript !== 'firstPerson') {
        loadModelScript(initFirstPersonScript, disposeThirdPersonScript, '3DModel-First-Person.js', 'model-container');
        activeScript = 'firstPerson';
    }
});

// Event listener for the Third Person button
document.getElementById('thirdPersonBtn').addEventListener('click', function () {
    // Load the third person script if it's not already active
    if (activeScript !== 'thirdPerson') {
        loadModelScript(initThirdPersonScript, disposeFirstPersonScript, '3DModel-Third-Person.js', 'model-container');
        activeScript = 'thirdPerson';
    }
});

document.getElementById('thirdPersonBtn').click();