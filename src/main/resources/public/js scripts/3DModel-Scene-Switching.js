import * as THREE from 'three';
import {OrbitControls} from 'three/addons/controls/OrbitControls.js';
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js';
import {FBXLoader} from 'three/addons/loaders/FBXLoader.js';
import {OBJLoader} from 'three/addons/loaders/OBJLoader.js';
import {setCameraPosition} from "../js scripts/3DModels-Set-Camera-Position.js";

let scene;
export {scene};
let renderer;
let model;
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car');
let timeOut;
let centerPointPosition;
let isThirdPerson = false;
let showroom;
export {showroom};

function initThirdPersonScript() {
    isThirdPerson = true;
    showLoadingOverlay();
    showControlsInfo("third-person");
    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();
    const aspectRatio = containerRect.width / containerRect.height;
    const camera = new THREE.PerspectiveCamera(75, aspectRatio, 15, 215000);
    renderer = new THREE.WebGLRenderer();
    const controls = new OrbitControls(camera, renderer.domElement);
    // Enable collision detection
    controls.enableDamping = true; // Optional, adds inertia to camera movement
    controls.enablePan = true; // Enable panning
    controls.enableZoom = true; // Enable zooming
    controls.enableRotate = true; // Enable rotation
    controls.enableKeys = false; // Disable keyboard controls to prevent accidental movements
    controls.autoRotate = false; // Disable auto-rotation
    controls.screenSpacePanning = true; // Use screen space for panning
    controls.enableCollision = true; // Enable collision detection to prevent camera from intersecting with the model

    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);

    let autoRotate = true;

    // Create a pivot object
    const pivot = new THREE.Group();
    scene.add(pivot);

    let loader;

    // Check if the model is an FBX, OBJ or GLB
    if (carParam.includes('.glb') || carParam.includes('.gltf')) {
        loader = new GLTFLoader();
        loader.load("3D Models/Showroom.glb", (gltf) => {
            showroom = gltf.scene;
            showroom.scale.set(1, 1, 1); // Adjust scale as needed
            showroom.traverse((child) => {
                if (child.name === "Object_36") {
                    console.log(`yes`);
                    centerPointPosition = child.position.clone();
                }
                if (child.isMesh) {
                    // Check if the material is already a MeshStandardMaterial
                    if (child.material.isMeshStandardMaterial) {
                        // Adjust material properties for a metallic appearance
                        child.material.metalness = 0.9; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                        child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                    }
                }
            });
            // Center the showroom model
            const boundingBox = new THREE.Box3().setFromObject(showroom);
            boundingBox.getCenter(showroom.position);
            showroom.position.multiplyScalar(-1);

            // Add the showroom model to the scene
            scene.add(showroom);
        }, undefined, (error) => {
            console.error('Error loading showroom:', error);
        });
        loader.load(carParam, (gltf) => {
            model = gltf.scene;

            // Center the geometry based on its bounding box
            const boundingBox = new THREE.Box3().setFromObject(model);
            boundingBox.getCenter(model.position);
            model.position.multiplyScalar(-1);

            // Add the model to the pivot
            pivot.add(model);
            if (carParam.includes`Porsche-Carrera-2015.glb`) {
                model.traverse(child => {
                    if (child.isMesh) {
                        const material = child.material;
                        if (material) {
                            material.metalness = 0.8;
                            material.roughness = 0.5;
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Urus-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.5; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mclaren-Senna-2020.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-G-Class-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Jeep-Compass-2020.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-SL-Class-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Porsche-GT3 RS-2023.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Subaru-Impreza-1998.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-Maybach GLS 600-2023.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-M5-1999.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-X5-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-M4-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Aventador-2019.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties;
                            child.material.metalness = 0.7 // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Porsche-Boxster-2016.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.5; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-S-Class-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.5; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Rolls-Royce-Ghost-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mclaren-P1-2015.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Tesla-Model-3-2020.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Nissan-GT-R-1998.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Ferrari-F40-1992.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-Brabus 800 S63-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.5; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            }
            setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, controls, scene, pivot); // Set the camera position after loading the model
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
                setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, controls, scene, pivot);
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
            (obj) => {
                scene.add(obj);
                model = obj;
                setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, controls, scene, pivot);
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
        if (model && autoRotate && isThirdPerson) {
            // Rotate the pivot around the Y-axis
            model.rotation.y += 0.005;
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
    controls.addEventListener('end', () => {
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
            autoRotate = true;
        }, 10500);
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
    isThirdPerson = false;
    showControlsInfo("first-person");
    const container = document.getElementById('model-container');
    const containerRect = container.getBoundingClientRect();
    scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(75, containerRect.width / containerRect.height, 0.1, 15000);
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(containerRect.width, containerRect.height);
    container.appendChild(renderer.domElement);
    scene.add(camera);
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
            case `W`:
            case `Ш`:
            case `ш`:
                moveForward = true;
                break;
            case 's':
            case `S`:
            case `С`:
            case `с`:
                moveBackward = true;
                break;
            case 'a':
            case `A`:
            case `А`:
            case `а`:
                moveLeft = true;
                break;
            case 'd':
            case `D`:
            case `Д`:
            case `д`:
                moveRight = true;
                break;
        }
    };

    const onKeyUp = (event) => {
        switch (event.key) {
            case 'w':
            case `W`:
            case `Ш`:
            case `ш`:
                moveForward = false;
                break;
            case 's':
            case `S`:
            case `с`:
            case `С`:
                moveBackward = false;
                break;
            case 'a':
            case `A`:
            case `А`:
            case `а`:
                moveLeft = false;
                break;
            case 'd':
            case `D`:
            case `Д`:
            case `д`:
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
        loader.load("3D Models/Showroom.glb", (gltf) => {
            showroom = gltf.scene;
            showroom.scale.set(1, 1, 1); // Adjust scale as needed
            showroom.traverse((child) => {
                if (child.isMesh) {
                    if (child.name === "Object_36") {
                        centerPointPosition = child.position.clone();
                    }
                    if (child.material.isMeshStandardMaterial) {
                        // Adjust material properties for a metallic appearance
                        child.material.metalness = 0.9; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                        child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                    }
                }
            });
            // Center the showroom model
            const boundingBox = new THREE.Box3().setFromObject(showroom);
            boundingBox.getCenter(showroom.position);
            showroom.position.multiplyScalar(-1);

            // Add the showroom model to the scene
            scene.add(showroom);
        }, undefined, (error) => {
            console.error('Error loading showroom:', error);
        });
        loader.load(carParam, (gltf) => {
            model = gltf.scene;
            scene.add(model);

            if (carParam.includes`Porsche-Carrera-2015.glb`) {
                model.traverse(child => {
                    if (child.isMesh) {
                        const material = child.material;
                        if (material) {
                            material.metalness = 0.8;
                            material.roughness = 0.5;
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-SL-Class-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-G-Class-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Jeep-Compass-2020.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Porsche-GT3 RS-2023.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`McLaren-F1 GTR-1995.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Subaru-Impreza-1998.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-Brabus 800 S63-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Urus-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.4; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.5; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-S-Class-2022.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.4; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-Maybach GLS 600-2023.glb`)) {
                model.traverse(child => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Gallardo-2007.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // 0 for non-metallic, 1 for fully metallic
                            child.material.roughness = 0.3; // 0 for a smooth surface, 1 for a rough surface
                        }
                    }
                });
            } else if (carParam.includes(`Toyota-Supra-Gr-2020.glb`)) {
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
            } else if (carParam.includes(`Mclaren-P1-2015.glb`)) {
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
            } else if (carParam.includes(`Tesla-Model-3-2020.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-X5-2022.glb`) ||
                carParam.includes(`Bugatti-Chiron-2005.glb`) ||
                carParam.includes(`Ford-F-150-2022.glb`) ||
                carParam.includes(`Jeep-Grand Cherokee SRT-2017.glb`) ||
                carParam.includes(`Nissan-GT-R-2017.glb`)) {

                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Aventador-2019.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-M4-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Lamborghini-Murcielago-2010.glb`) ||
                carParam.includes(`Volkswagen-Golf-2021.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.2; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Porsche-Boxster-2016.glb`) ||
                carParam.includes(`McLaren-P1-2019.glb`) ||
                carParam.includes(`Mercedes-Benz-E-Class-2014.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-Brabus G900-2023.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.7; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Mercedes-Benz-SLS AMG GT Final Edition-2020.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Rolls-Royce-Ghost-2022.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.3; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 100; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`BMW-M5-1999.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.9; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 150; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Ferrari-F40-1992.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else if (carParam.includes(`Porsche-918 Spyder-2015.glb`)) {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.8; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 200; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            } else {
                gltf.scene.traverse((child) => {
                    if (child.isMesh) {
                        // Check if the material is already a MeshStandardMaterial
                        if (child.material.isMeshStandardMaterial) {
                            // Adjust material properties
                            child.material.metalness = 0.6; // Adjust metalness (0 for non-metallic, 1 for fully metallic)
                            child.material.roughness = 0.4; // Adjust roughness (0 for a smooth surface, 1 for a rough surface)
                        } else if (child.material.isMeshPhongMaterial) {
                            // For MeshPhongMaterial
                            child.material.specular = new THREE.Color(0x555555); // Adjust specular color for shininess
                            child.material.shininess = 150; // Adjust shininess (higher values for shinier)
                        }
                    }
                });
            }
            animate();
            setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, scene);
            hideLoadingOverlay();
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

    scene.add(camera);

    function animate() {
        requestAnimationFrame(animate);

        // Move the camera based on the keyboard input
        let moveSpeed = 0.2;

        if (carParam.includes(`Porsche-Carrera-2015.glb`)) {
            moveSpeed = 0.009;
        } else if (carParam.includes(`Lamborghini-Aventador-2020.glb`)) {
            moveSpeed = 1;
        } else if (carParam.includes(`Lamborghini-Urus-2022.glb`)) {
            moveSpeed = 0.009;
        } else if (carParam.includes(`BMW-M4-2022.glb`) ||
            carParam.includes(`Ford-F-150-2022.glb`) ||
            carParam.includes(`Jeep-Grand Cherokee SRT-2017.glb`) ||
            carParam.includes(`Lamborghini-Aventador-2019.glb`)) {
            moveSpeed = 0.8;
        } else if (carParam.includes(`Tesla-Model-3-2020.glb`)) {
            moveSpeed = 0.8;
        } else if (carParam.includes(`BMW-X5-2022.glb`) ||
            carParam.includes(`Mercedes-Benz-SLS AMG GT Final Edition-2020.glb`) ||
            carParam.includes(`Nissan-GT-R-2017.glb`)) {
            moveSpeed = 0.5;
        } else if (carParam.includes(`Porsche-918 Spyder-2015.glb`)) {
            moveSpeed = 0.1;
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
        let interactedWithCanvas = false; // Flag to track canvas interaction

        function handleTouchStart(event) {
            if (event.target.tagName.toLowerCase() === 'canvas') {
                interactedWithCanvas = true; // Set the flag to true when interacting with the canvas
            } else {
                return;
            }
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
            if (interactedWithCanvas) {
                event.preventDefault(); // Prevent default scrolling if interacting with the canvas
            } else {
                return;
            }
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

            } else if (fingerCount === 2) {
                const touch1 = event.touches[0];
                const touch2 = event.touches[1];
                const pinchDistance = Math.hypot(touch1.clientX - touch2.clientX, touch1.clientY - touch2.clientY);

                // Adjust the camera position based on the pinch distance
                let moveSpeed = 0;

                if (carParam.includes(`Porsche-Carrera-2015.glb`)) {
                    moveSpeed = 0.08;
                } else if (carParam.includes(`Lamborghini-Aventador-2020.glb`) ||
                    carParam.includes(`Tesla-Model-3-2020.glb`) || carParam.includes(`BMW-X5-2022.glb`) ||
                    carParam.includes(`Jeep-Grand Cherokee SRT-2017.glb`)) {
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
            }
        }

        function handleTouchEnd(event) {
            if (interactedWithCanvas) {
                event.preventDefault(); // Prevent default scrolling if interacting with the canvas
            } else {
                return;
            }
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
        document.addEventListener('touchstart', handleTouchStart, {passive: false});
        document.addEventListener('touchmove', handleTouchMove, {passive: false});
        document.addEventListener('touchend', handleTouchEnd, {passive: false});

        document.addEventListener('touchstart', function (event) {
            if (event.target.tagName.toLowerCase() !== 'canvas') {
                interactedWithCanvas = false;
            }
        });
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
    const container = document.getElementById(`loader`);
    const loadingOverlayContainer = document.createElement('div');
    loadingOverlayContainer.id = 'loading-overlay-container';
    loadingOverlayContainer.style.display = `flex`;
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loading-overlay';
    loadingOverlay.innerHTML = '<div class="lds-ring"><div></div><div></div><div></div><div></div></div>';

    loadingOverlayContainer.appendChild(loadingOverlay);
    container.appendChild(loadingOverlayContainer);
}

function hideLoadingOverlay() {
    console.log('Hiding loading overlay');
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

document.getElementById('thirdPersonBtn').click();

function showControlsInfo(scriptName) {
    if (scriptName === "first-person") {
        document.getElementById("controlls-info").innerHTML = `
        <h3>3D Camera Controls:</h3>
        <p><strong>PC Controls:</strong></p>
        <ul>
            <li><strong>Rotate Camera:</strong> Hold down the left mouse button and drag to rotate the camera.</li>
            <li><strong>Move camera:</strong> Move by holding W A S D keys on your keyboard.</li>
        </ul>
        <p><strong>Mobile Controls:</strong></p>
        <ul>
            <li><strong>Rotate/Zoom/Move Camera:</strong> By touching and dragging on the screen.</li>
        </ul>
        `
    } else {
        document.getElementById("controlls-info").innerHTML = `
        <h3>3D Camera Controls:</h3>
        <p><strong>PC Controls:</strong></p>
        <ul>
            <li><strong>Rotate/Move Camera:</strong> Hold down the left/right mouse button and drag to rotate the camera.</li>
            <li><strong>Zoom In/Out:</strong> Use the scroll wheel to zoom the camera in or out.</li>
        </ul>
        <p><strong>Mobile Controls:</strong></p>
        <ul>
            <li><strong>Rotate/Zoom/Move Camera:</strong> By touching and dragging on the screen.</li>
        </ul>
        `
    }
}
