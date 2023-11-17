import * as THREE from 'https://cdn.skypack.dev/three@0.132.2';
import { GLTFLoader } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'https://cdn.skypack.dev/three@0.132.2/examples/jsm/controls/OrbitControls';

let scene;
let renderer;
let model;
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car');

function initThirdPersonScript() {

    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();

    const aspectRatio = containerRect.width / containerRect.height;
    const camera = new THREE.PerspectiveCamera(75, aspectRatio, 0.1, 15000);
    renderer = new THREE.WebGLRenderer();
    const controls = new OrbitControls(camera, renderer.domElement);

    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

    let autoRotate = true;

    // Create a pivot object
    const pivot = new THREE.Group();
    scene.add(pivot);

    function setCameraPosition() {
        const nearClip = 1; // Adjust this value based on your scene
        const farClip = 5000; // Adjust this value based on your scene

        // Set the near and far clipping planes for the camera
        camera.near = nearClip;
        camera.far = farClip;
        camera.updateProjectionMatrix();
        camera.fov = 30; // Adjust this value based on your scene
        camera.updateProjectionMatrix();
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
                if (carParam.includes(`lambo-aventador`)) {
                    camera.position.z += cameraDistance - 1910;
                } else if (carParam.includes(`modified_lamborghini_urus.glb`)) {
                    camera.position.z += cameraDistance - 145;
                } else {
                    camera.position.z += cameraDistance - 42;
                }
            } else {
                if (carParam.includes(`lambo-aventador`)) {
                    camera.position.z += cameraDistance - 920;
                } else {
                    camera.position.z += cameraDistance - 20;
                } 
            }

            // Set controls target
            controls.target.copy(center);
        }
    }

    const loader = new GLTFLoader();
    loader.load(carParam, (gltf) => {
        model = gltf.scene;

        // Center the geometry based on its bounding box
        const boundingBox = new THREE.Box3().setFromObject(model);
        boundingBox.getCenter(model.position);
        model.position.multiplyScalar(-1);

        // Add the model to the pivot
        pivot.add(model);
        if (carParam.includes`porsche`) {
            model.scale.set(165, 165, 165);
            model.traverse(child => {
                if (child.isMesh) {
                    const material = child.material;
                    if (material) {
                        material.metalness = 0.9; // Adjust metalness
                        material.roughness = 0.5; // Adjust roughness
                    }
                }
            });
            const directionalLight = new THREE.DirectionalLight(0xffffff, 3.5);
            directionalLight.position.set(5, 5, 5).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 3);
            scene.add(ambientLight);
        } else if (carParam.includes`lambo-aventador`) {
            model.scale.set(170, 170, 170);
            const directionalLight = new THREE.DirectionalLight(0xffffff, 2.5);
            directionalLight.position.set(5, 5, 5).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 5);
            scene.add(ambientLight);
        } else {
            const directionalLight = new THREE.DirectionalLight(0xffffff, 2.5);
            directionalLight.position.set(5, 5, 5).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 1);
            scene.add(ambientLight);
        }

        setCameraPosition(); // Set the camera position after loading the model

    }, undefined, (error) => {
        console.error('Error loading GLB model:', error);
    });

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
        // Dispose the 3D model logic
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

    const camera = new THREE.PerspectiveCamera(75, containerRect.width / containerRect.height, 0.1, 15000);
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

    const nearClip = 1; // Adjust this value based on your scene
    const farClip = 5000; // Adjust this value based on your scene

    // Set the near and far clipping planes for the camera
    camera.near = nearClip;
    camera.far = farClip;
    camera.updateProjectionMatrix();
    camera.fov = 30; // Adjust this value based on your scene
    camera.updateProjectionMatrix();

    let moveForward = false;
    let moveBackward = false;
    let moveLeft = false;
    let moveRight = false;

    let mouseDown = false;
    let prevMouseX = 0;
    let prevMouseY = 0;

    const turnSpeedX = 0.002;
    const turnSpeedY = 0.002;
    const maxVerticalRotation = Math.PI / 3;

    // Set the camera rotation order to "YXZ" to prevent flipping
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
    loader.load(carParam, (gltf) => {
        model = gltf.scene;
        scene.add(model);

        if (carParam.includes`porsche`) {
            model.scale.set(160, 160, 160);
            camera.position.set(0, 3, 20);
            model.traverse(child => {
                if (child.isMesh) {
                    const material = child.material;
                    if (material) {
                        material.metalness = 0.9; // Adjust metalness
                        material.roughness = 0.5; // Adjust roughness
                    }
                }
            });
            const directionalLight = new THREE.DirectionalLight(0xffffff, 4.5);
            directionalLight.position.set(5, 5, 5).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 5);
            scene.add(ambientLight);
        } else if (carParam.includes`lambo-aventador`) {
            model.scale.set(160, 160, 160);
            camera.position.set(0, 100, 850);
            const directionalLight = new THREE.DirectionalLight(0xffffff, 5.5);
            directionalLight.position.set(5, 5, 5).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 5);
            scene.add(ambientLight);
        } else if (carParam.includes(`modified_lamborghini_urus`)) {
            camera.position.set(10.5,10,60)
        }

        const directionalLight = new THREE.DirectionalLight(0xffffff, 3.5);
        directionalLight.position.set(5, 5, 5).normalize();
        scene.add(directionalLight);

        const ambientLight = new THREE.AmbientLight(0xffffff, 2);
        scene.add(ambientLight);

        animate();
    }, undefined, (error) => {
        console.error('Error loading GLB model:', error);
    });

    function animate() {
        requestAnimationFrame(animate);

        // Move the camera based on the keyboard input
        let moveSpeed = 0.1;

        if (carParam.includes(`porsche`)) {
            moveSpeed = 0.02;
        } else if (carParam.includes`lambo-aventador`) {
            moveSpeed = 1;
        }
        // Calculate the movement vectors in the cameras local coordinate system
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

    const isMobile = /Mobi|Android/i.test(navigator.userAgent);

    if (isMobile) {
        let touchStartX = 0;
        let touchStartY = 0;
        let pinchStartDistance = 0;
        let fingerCount = 0;
        let lockTime = 0;
    
        function handleTouchStart(event) {
            fingerCount = event.touches.length;
    
            if (fingerCount === 1) {
                touchStartX = event.touches[0].clientX;
                touchStartY = event.touches[0].clientY;
            } else if (fingerCount === 2) {
                const touch1 = event.touches[0];
                const touch2 = event.touches[1];
                pinchStartDistance = Math.hypot(touch1.clientX - touch2.clientX, touch1.clientY - touch2.clientY);
            }
        }
    
        function handleTouchMove(event) {
            if (fingerCount === 1) {
                const touchX = event.touches[0].clientX;
                const touchY = event.touches[0].clientY;
    
                const deltaX = touchX - touchStartX;
                const deltaY = touchY - touchStartY;
    
                // Adjust camera rotation based on touch movement
                camera.rotation.y -= deltaX * 0.005;
    
                // Clamp vertical rotation to prevent flipping
                const turnSpeedY = 0.005;
                camera.rotation.x = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, camera.rotation.x - deltaY * turnSpeedY));
    
                touchStartX = touchX;
                touchStartY = touchY;
    
                event.preventDefault();
            } else if (fingerCount === 2) {
                const touch1 = event.touches[0];
                const touch2 = event.touches[1];
                const pinchDistance = Math.hypot(touch1.clientX - touch2.clientX, touch1.clientY - touch2.clientY);
    
                // Adjust the camera position based on the pinch distance
                const moveSpeed = 0.10;
                const moveDistance = pinchStartDistance - pinchDistance;
    
                // Calculate the movement vectors in the camera's local coordinate system
                const frontVector = new THREE.Vector3(0, 0, -1).applyQuaternion(camera.quaternion);
    
                // Move the camera forward or backward based on the pinch gesture
                camera.position.addScaledVector(frontVector, moveDistance * moveSpeed);
    
                pinchStartDistance = pinchDistance;
    
                event.preventDefault();
            }
        }
    
        function handleTouchEnd(event) {
            if (fingerCount === 2) {
                // Lock camera movement for a short duration after lifting both fingers
                lockTime = Date.now() + 500; // 500 milliseconds lock time
            }
        }
    
        function animate() {
            requestAnimationFrame(animate);
    
            // Check if the lock time has passed
            if (Date.now() > lockTime) {
                // Render the scene only if not in the lock period
                renderer.render(scene, camera);
            }
        }
    
        // Add passive: false to the touch event listeners to disable browser default scrolling
        document.addEventListener('touchstart', handleTouchStart, { passive: false });
        document.addEventListener('touchmove', handleTouchMove, { passive: false });
        document.addEventListener('touchend', handleTouchEnd, { passive: false });
    
        animate();
    }    

}

function disposeFirstPersonScript() {
    if (model) {
        // Dispose the 3D model logic
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

        container.appendChild(renderer.domElement);

        // Update the script to the current renderer
        activeScript = renderer;
    } else {
        console.error(`Container with ID ${containerId} not found.`);
    }
}

// Event listener for the First Person button
document.getElementById('firstPersonBtn').addEventListener('click', function () {
    // Load the first person script if its not already active
    if (activeScript !== 'firstPerson') {
        loadModelScript(initFirstPersonScript, disposeThirdPersonScript, '3DModel-First-Person.js', 'model-container');
        activeScript = 'firstPerson';
    }
});

// Event listener for the Third Person button
document.getElementById('thirdPersonBtn').addEventListener('click', function () {
    // Load the third person script if its not already active
    if (activeScript !== 'thirdPerson') {
        loadModelScript(initThirdPersonScript, disposeFirstPersonScript, '3DModel-Third-Person.js', 'model-container');
        activeScript = 'thirdPerson';
    }
});

document.getElementById('thirdPersonBtn').click();