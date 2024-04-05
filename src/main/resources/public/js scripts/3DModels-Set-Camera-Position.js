import * as THREE from 'three';
import {hideLoadingOverlay, scene, showroom} from "../js scripts/3DModel-Scene-Switching.js";

export function setCameraPosition(centerPointPosition, camera, model, carParam, containerRect, controls, pivot) {
    const nearClip = 0.1;

    // Set the near and far clipping planes for the camera
    camera.near = nearClip;
    camera.fov = 30;
    camera.far = 111100;
    camera.updateProjectionMatrix();

    if (model && centerPointPosition) {
        // Add the model as a child of the showroom
        showroom.add(model);

        // Calculate bounding boxes of the model and the showroom
        const modelBoundingBox = new THREE.Box3().setFromObject(model);
        const showroomBoundingBox = new THREE.Box3().setFromObject(showroom);

        let quantifier = 0.14;

        if (carParam.includes(`Tesla`)) {
            quantifier = 0.004
        } else if (carParam.includes("Final Edition-2020")) {
            quantifier = 0.10;
        } else if (carParam.includes(`Aventador-2019`) ||
            carParam.includes("Lamborghini-Murcielago-2010.glb")) {
            quantifier = 0.08;
        } else if (carParam.includes(`Aventador-2020`) ||
            carParam.includes(`Gallardo`) ||
            carParam.includes(`BMW-X5`) ||
            carParam.includes(`Mclaren-P1`) ||
            carParam.includes(`Grand Cherokee`) ||
            carParam.includes(`Ghost-2022`) ||
            carParam.includes(`F-150-2022`) ||
            carParam.includes(`Aventador-2019`) ||
            carParam.includes("Volkswagen-Golf-2021.glb")) {
            quantifier = 0.11;
        }

        // Calculate the scaling factor required to fit the model inside the showroom
        const modelSize = modelBoundingBox.getSize(new THREE.Vector3());
        const showroomSize = showroomBoundingBox.getSize(new THREE.Vector3());
        const scale = Math.min(
            showroomSize.x / modelSize.x,
            showroomSize.y / modelSize.y,
            showroomSize.z / modelSize.z
        ) * quantifier; // Adjusting scale to make the model smaller

        // Apply scale to the model
        model.scale.set(scale, scale, scale);

        // Calculate the required position adjustments to center the model inside the showroom
        const modelCenter = new THREE.Vector3();
        modelBoundingBox.getCenter(modelCenter);
        const showroomCenter = new THREE.Vector3();
        showroomBoundingBox.getCenter(showroomCenter);

        // Adjust position to center the model inside the showroom
        model.position.sub(modelCenter.multiplyScalar(scale)); // Apply scale to modelCenter
        model.position.add(showroomCenter);

        // Adjust position to make model touch the centerPointPosition
        model.position.y = centerPointPosition.y;
        if (carParam.includes(`Gallardo`) ||
            carParam.includes(`Tesla`) ||
            carParam.includes(`BMW-X5`) ||
            carParam.includes(`Grand Cherokee`) ||
            carParam.includes(`G900`) ||
            carParam.includes(`M5-1999`) ||
            carParam.includes(`Ghost-2022`) ||
            carParam.includes(`GT3 RS-2023`) ||
            carParam.includes(`F-150-2022`) ||
            carParam.includes(`GT-R-2017`) ||
            carParam.includes("Final Edition-2020") ||
            carParam.includes("Volkswagen-Golf-2021.glb") ||
            carParam.includes("Lamborghini-Murcielago-2010.glb")) {
            alignModel(carParam, model);
        }

        // Update pivot position
        if (pivot !== undefined) {
            pivot.position.copy(centerPointPosition);
        }

        // Step 4: Position the camera
        const distance = 7; // Distance from the model
        const cameraOffset = new THREE.Vector3(0, 0, distance);
        camera.position.copy(model.position).add(cameraOffset);
        camera.position.y += -2;

        // Set controls target
        if (controls.target !== undefined) {
            controls.target.copy(showroomCenter);
            if (!carParam.includes(`Tesla`) &&
                !carParam.includes(`F-150-2022`)) {
                controls.target.y += -2;
            }
        }

        // Add lights to the scene
        const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
        directionalLight.position.set(0, 1, 0).normalize();
        scene.add(directionalLight);

        const directionalLightRight = new THREE.DirectionalLight(0xffffff, 1);
        directionalLightRight.position.set(1, 0, 0);
        scene.add(directionalLightRight);

        const directionalLightLeft = new THREE.DirectionalLight(0xffffff, 1);
        directionalLightLeft.position.set(-1, 0, 0);
        scene.add(directionalLightLeft);

        const ambientLight = new THREE.AmbientLight(0xffffff, 1);
        scene.add(ambientLight);

        hideLoadingOverlay();
    }

    function alignModel(carParam, model) {
        if (carParam.includes(`Tesla`)) {
            model.position.y += 0.3;
        } else if (carParam.includes(`Gallardo`) ||
            carParam.includes(`BMW-X5`) ||
            carParam.includes(`M5-1999`) ||
            carParam.includes("Final Edition-2020")) {
            model.position.y += 0.1
        } else if (carParam.includes(`G900`)) {
            model.position.y += -0.03
        } else if (carParam.includes(`Grand Cherokee`)) {
            model.position.y += 0.4
        } else if (carParam.includes(`Ghost-2022`) ||
            carParam.includes("Lamborghini-Murcielago-2010.glb")) {
            model.position.y += 0.02
        } else if (carParam.includes(`GT3 RS-2023`) ||
            carParam.includes("Volkswagen-Golf-2021.glb")) {
            model.position.y += 0.05
        } else if (carParam.includes(`F-150-2022`)) {
            model.position.y += 0.44;
        } else if (carParam.includes(`GT-R-2017`)) {
            model.position.y += -0.02;
        }
    }
}
