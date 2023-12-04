import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { FBXLoader } from 'three/addons/loaders/FBXLoader.js';
import { OBJLoader } from 'three/addons/loaders/OBJLoader.js';

let scene;
let renderer;
let model;
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car');

function initThirdPersonScript() {
    showLoadingOverlay();
    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();

    const aspectRatio = containerRect.width / containerRect.height;
    const camera = new THREE.PerspectiveCamera(75, aspectRatio, 15, 25000);
    renderer = new THREE.WebGLRenderer();
    const controls = new OrbitControls(camera, renderer.domElement);

    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

    let autoRotate = true;

    // Create a pivot object
    const pivot = new THREE.Group();
    scene.add(pivot);
    let cameraLight;

    function setCameraPosition() {
        const nearClip = 1;

        // Set the near and far clipping planes for the camera
        camera.near = nearClip;
        camera.updateProjectionMatrix();
        camera.fov = 30;
        camera.updateProjectionMatrix();
        if (model) {
            // Calculate bounding box of the model
            const boundingBox = new THREE.Box3().setFromObject(model);
            const center = new THREE.Vector3();
            boundingBox.getCenter(center);
            model.position.sub(center);

            pivot.position.copy(center);
            // distance from the center to the camera position
            const boundingBoxSize = boundingBox.getSize(new THREE.Vector3());
            const cameraDistance = Math.max(boundingBoxSize.x, boundingBoxSize.y, boundingBoxSize.z) /
                Math.tan(THREE.MathUtils.degToRad(camera.fov / 2));

            // Set camera position
            camera.position.copy(center);
            if (containerRect.width > 430) {
                if (carParam.includes(`lambo-aventador`)) {
                    camera.position.z += cameraDistance - 1910;
                } else if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
                    camera.position.z += cameraDistance - 285;
                } else if (carParam.includes(`gallardo`)) {
                    camera.position.z += cameraDistance - 490;
                } else if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                    carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                    camera.position.z += cameraDistance - 1200;
                } else if (carParam.includes(`gr_supra`)) {
                    camera.position.z += cameraDistance - 570;
                } else if (carParam.includes(`2015_-_porsche_911_carrera_s__mid-poly (1).glb`)) {
                    camera.position.z += cameraDistance - 40;
                } else {
                    camera.position.z += cameraDistance - 40;
                }
            } else {
                if (carParam.includes(`lambo-aventador`)) {
                    camera.position.z += cameraDistance - 920;
                } else if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
                    camera.position.z += cameraDistance - 145;
                } else if (carParam.includes(`gallardo`)) {
                    camera.position.z += cameraDistance - 240;
                } else if (carParam.includes(`gr_supra`)) {
                    camera.position.z += cameraDistance - 250;
                } else if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                    carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                    camera.position.z += cameraDistance - 550;
                } else {
                    camera.position.z += cameraDistance - 20;
                }
            }

            // Set controls target
            controls.target.copy(center);

            if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
                cameraLight = new THREE.SpotLight(0xffffff, 1);
            } else if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                cameraLight = new THREE.SpotLight(0xffffff, 0.2);
            } else {
                cameraLight = new THREE.SpotLight(0xffffff, 1);
            }

            cameraLight.position.copy(camera.position);
            cameraLight.target.position.copy(controls.target);
            scene.add(cameraLight);
            scene.add(cameraLight.target);

            if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                const directionalLight = new THREE.DirectionalLight(0xffffff, 0.1);
                directionalLight.position.set(camera.position).normalize();
                scene.add(directionalLight);
            } else {
                const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
                directionalLight.position.set(camera.position).normalize();
                scene.add(directionalLight);
            }

            if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
                scene.add(ambientLight);
            } else {
                const ambientLight = new THREE.AmbientLight(0xffffff, 1);
                scene.add(ambientLight);
            }
        }
    }

    let loader;

    // Check if the model is an FBX, OBJ or GLB 
    if (carParam.includes('.glb')) {
        loader = new GLTFLoader();
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
                            material.metalness = 0.9;
                            material.roughness = 0.5;
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
            } else if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
                model.scale.set(20, 20, 20);
                model.traverse(child => {
                    if (child.isMesh) {
                        const material = child.material;
                        if (material) {
                            material.metalness = 0.1;
                            material.roughness = 0.5;
                        }
                    }
                });
            } else {
                model.scale.set(40, 40, 40);
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.5; // 0 for non-metallic, 1 for fully metallic
                            child.material.roughness = 0.2; // 0 for a smooth surface, 1 for a rough surface
                        }
                    }
                });
            }

            setCameraPosition(); // Set the camera position after loading the model
            hideLoadingOverlay();
        }, undefined, (error) => {
            console.error('Error loading GLB model:', error);
            hideLoadingOverlay();
        });

    } else if (carParam.includes('.fbx') || carParam.includes('.FBX')) {
        loader = new FBXLoader();
        loader.load(
            carParam,
            (fbx) => {
                hideLoadingOverlay();
                scene.add(fbx);
                model = fbx;
                setCameraPosition();
                pivot.add(model);
            },
            undefined,
            (error) => {
                console.error('Error loading FBX model:', error);
                hideLoadingOverlay();
            }
        );
    } else {
        const loadingManager = new THREE.LoadingManager(() => {
            // This function is called when all resources are loaded to hide the loading animation
            hideLoadingOverlay();
        });
        loader = new OBJLoader(loadingManager);
        loader.load(
            carParam,
            (fbx) => {
                scene.add(fbx);
                model = fbx;
                setCameraPosition();
                pivot.add(model);
            },
            undefined,
            (error) => {
                console.error('Error loading FBX model:', error);
                hideLoadingOverlay();
            }
        );
    }

    function animate() {
        requestAnimationFrame(animate);
        if (cameraLight) {
            cameraLight.position.copy(camera.position);
            cameraLight.target.position.copy(controls.target);
        }
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
            }
        });
        scene.remove(model);
        model = null; // Clear the global variable
    }
}

// First Person Script
function initFirstPersonScript() {
    showLoadingOverlay();
    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();

    const camera = new THREE.PerspectiveCamera(75, containerRect.width / containerRect.height, 0.1, 15000);
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

    const nearClip = 1;
    const farClip = 5000;
    camera.near = nearClip;
    camera.far = farClip;
    camera.updateProjectionMatrix();
    camera.fov = 30;
    camera.updateProjectionMatrix();

    let cameraLight;
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

    let loader;
    // Check if the model is an FBX or GLB
    if (carParam.includes('.glb')) {
        loader = new GLTFLoader();
        loader.load(carParam, (gltf) => {
            model = gltf.scene;
            scene.add(model);

            if (carParam.includes`porsche`) {
                model.scale.set(300, 300, 300);
                camera.position.set(0, 5, 35);
                model.traverse(child => {
                    if (child.isMesh) {
                        const material = child.material;
                        if (material) {
                            material.metalness = 0.9;
                            material.roughness = 0.5;
                        }
                    }
                });
                const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5);
                directionalLight.position.set(5, 5, 5).normalize();
                scene.add(directionalLight);

                const ambientLight = new THREE.AmbientLight(0xffffff, 1);
                scene.add(ambientLight);
            } else if (carParam.includes`lambo-aventador`) {
                model.scale.set(160, 160, 160);
                camera.position.set(0, 100, 850);
                const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5);
                directionalLight.position.set(5, 5, 5).normalize();
                scene.add(directionalLight);

                const ambientLight = new THREE.AmbientLight(0xffffff, 1);
                scene.add(ambientLight);
            } else if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
                camera.position.set(0, 20, 125)
                model.scale.set(20, 20, 20);
                model.traverse(child => {
                    if (child.isMesh) {
                        const material = child.material;
                        if (material) {
                            material.metalness = 0.1;
                            material.roughness = 0.5;
                        }
                    }
                });
            } else if (carParam.includes(`bmw`)) {
                camera.position.set(15, 5, 15);
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // 0 for non-metallic, 1 for fully metallic
                            child.material.roughness = 0.5; // 0 for a smooth surface, 1 for a rough surface
                        }
                    }
                });
            } else if (carParam.includes(`gallardo`)) {
                camera.position.set(0, 10, 90);
                model.scale.set(15, 15, 15);
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // 0 for non-metallic, 1 for fully metallic
                            child.material.roughness = 0.5; // 0 for a smooth surface, 1 for a rough surface
                        }
                    }
                });
            } else if (carParam.includes(`gr_supra`)) {
                camera.position.set(0, 15, 105);
                model.scale.set(15, 15, 15);
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // 0 for non-metallic, 1 for fully metallic
                            child.material.roughness = 0.5; // 0 for a smooth surface, 1 for a rough surface
                        }
                    }
                });
            }

            const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5);
            directionalLight.position.set(0, 20, 125).normalize();
            scene.add(directionalLight);

            const ambientLight = new THREE.AmbientLight(0xffffff, 1);
            scene.add(ambientLight);

            hideLoadingOverlay();
            animate();
        }, undefined, (error) => {
            console.error('Error loading GLB model:', error);
            hideLoadingOverlay();
        });
    } else if (carParam.includes('.fbx') || carParam.includes('.FBX')) {
        const loadingManager = new THREE.LoadingManager(() => {
            // This function is called when all resources are loaded
            hideLoadingOverlay();
        });
        loader = new FBXLoader(loadingManager);
        loader.load(
            carParam,
            (fbx) => {
                scene.add(fbx);
                model = fbx;
                const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
                directionalLight.position.set(0, 20, 125).normalize();
                scene.add(directionalLight);

                const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
                scene.add(ambientLight);
                if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
                    carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
                    camera.position.set(0, 100, 600);
                }
                animate();
            },
            undefined,
            (error) => {
                console.error('Error loading FBX model:', error);
                hideLoadingOverlay();
            }
        );
    } else {
        console.error('Unsupported model format');
    }

    function animate() {
        requestAnimationFrame(animate);

        if (cameraLight) {
            cameraLight.position.copy(camera.position);
            cameraLight.target.position.copy(camera.position);
        }

        // Move the camera based on the keyboard input
        let moveSpeed = 0.2;

        if (carParam.includes(`porsche`)) {
            moveSpeed = 0.05;
        } else if (carParam.includes`lambo-aventador`) {
            moveSpeed = 1;
        } else if (carParam.includes(`lamborghini_urus_graphite_capsule.glb`)) {
            moveSpeed = 0.16;
        } else if (carParam.includes(`Porsche_911_Turbo_S_Coupe_2016.FBX`) ||
            carParam.includes(`Lincoln_Navigator_(Mk4)_(U554)_Black_Label_HQinterior_2017.FBX`)) {
            moveSpeed = 0.8;
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
                let moveSpeed = 0;

                if (carParam.includes(`modified_lamborghini_urus.glb`)) {
                    moveSpeed = 0.10;
                } else if (carParam.includes(`2015_-_porsche_911_carrera_s__mid-poly (1).glb`)) {
                    moveSpeed = 0.08;
                } else if (carParam.includes(`lambo-aventador.glb`)) {
                    moveSpeed = 0.35;
                } else {
                    moveSpeed = 0.11;
                }

                const moveDistance = pinchStartDistance - pinchDistance;

                // Calculate the movement vectors in the cameras local coordinate system
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

function showLoadingOverlay() {
    console.log('Showing loading overlay');
    const container = document.getElementById('loader');
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loading-overlay';
    loadingOverlay.innerHTML = '<div class="lds-ring"><div></div><div></div><div></div><div></div></div>';

    container.appendChild(loadingOverlay);
}

function hideLoadingOverlay() {
    console.log('Hiding loading overlay');

    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

document.getElementById('thirdPersonBtn').click();